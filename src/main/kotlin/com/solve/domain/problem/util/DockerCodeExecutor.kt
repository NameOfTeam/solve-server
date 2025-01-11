package com.solve.domain.problem.util

import com.solve.domain.problem.domain.entity.ProblemSubmit
import com.solve.domain.problem.domain.enums.ProblemSubmitLanguage
import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.problem.dto.request.ProblemSubmitRequest
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.util.CodeExecutor.ExecutionResult
import com.solve.global.config.file.FileProperties
import com.solve.global.error.CustomException
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class DockerCodeExecutor(
    private val submit: ProblemSubmit,
    private val request: ProblemSubmitRequest,
    private val fileProperties: FileProperties
) {
    data class ExecutionResult(
        val output: String,
        val error: String,
        val success: Boolean,
        val state: ProblemSubmitState? = null,
        val timeUsage: Long = 0,
        val compilationOutput: String? = null,
        val memoryUsage: Long = 0,
    )

    private val pythonContainerName = "python-judge"
    private val javaContainerName = "java-judge"

    private fun createSourceFile(): File {
        val directory = File(fileProperties.path, "submits").apply {
            if (!exists()) mkdirs()
        }
        val restoredCode = request.code.replace("\\n", "\n").replace("\\\"", "\"")

//        return File(directory, "${submit.id}.${getFileExtension()}").apply {
        return File(directory, "Main.${getFileExtension()}").apply {
            createNewFile()
            writeText(restoredCode)
        }
    }

    private fun getName() = when (request.language) {
        ProblemSubmitLanguage.PYTHON -> "python"
        ProblemSubmitLanguage.JAVA -> "java"
        else -> throw CustomException(ProblemError.LANGUAGE_NOT_SUPPORTED)
    }

    private fun getFileExtension() = when (request.language) {
        ProblemSubmitLanguage.PYTHON -> "py"
        ProblemSubmitLanguage.JAVA -> "java"
        ProblemSubmitLanguage.C -> "c"
        else -> throw CustomException(ProblemError.LANGUAGE_NOT_SUPPORTED)
    }

    fun execute(input: String, timeLimit: Double, expectedOutput: String): ExecutionResult {
        val sourceFile = createSourceFile()

//        compile(sourceFile)?.let { return it }

        val scriptPath = "/app/cmd/${getFileExtension()}_execute.sh"

        val command = listOf(
            "docker", "exec", "--privileged", "${getName()}-judge", "sh", "-c",
            "$scriptPath '${input.replace("'", "'\\''")}' Main.java"
        )

        println("Executing command: $command")

        val processBuilder = ProcessBuilder(command)
        val process = processBuilder.start()
        val output = StringBuilder()
        val error = StringBuilder()

        val outputThread = thread {
            process.inputStream.bufferedReader().useLines { lines ->
                lines.forEach { output.append(it).append("\n") }
            }
        }
        val errorThread = thread {
            process.errorStream.bufferedReader().useLines { lines ->
                lines.forEach { error.append(it).append("\n") }
            }
        }

        val timeLimitMillis = (timeLimit * 1000).toLong()
        val finishedInTime = process.waitFor(timeLimitMillis, TimeUnit.MILLISECONDS)

        outputThread.join(1000)
        errorThread.join(1000)

        if (!finishedInTime) {
            process.destroyForcibly()
            return ExecutionResult(
                output = "",
                error = "Time Limit Exceeded",
                success = false,
                state = ProblemSubmitState.TIME_LIMIT_EXCEEDED
            )
        }

        val errorOutput = error.toString().trim()
        val isPerfOutput = errorOutput.startsWith("Performance counter stats for")

        // 실제 에러가 아닌 경우 perf 메타데이터로 간주
        if (!isPerfOutput && errorOutput.isNotEmpty()) {
            println("Error Output: $errorOutput")
            return ExecutionResult(
                output = "",
                error = errorOutput,
                success = false,
                state = ProblemSubmitState.RUNTIME_ERROR
            )
        }

        val perfOutput = if (isPerfOutput) errorOutput else ""
        val entireOutput = output.toString().trim()

        // Perf 출력 이후의 내용 제거
        var actualOutput = entireOutput.substringBefore("Performance counter stats for").trim()
        actualOutput = actualOutput.replace(Regex("Memory Usage: .*"), "").trim()

        // 실행 시간 측정
        val timeRegex = Regex("(\\d+\\.\\d+) seconds time elapsed")
        val timeMatch = timeRegex.find(perfOutput)

        val timeUsage = timeMatch?.groups?.get(1)?.value?.toDoubleOrNull()?.let {
            (it * 1000).toInt().toLong()
        } ?: -1

        // 메모리 사용량 측정
        val memoryRegex = Regex("Memory Usage: (\\d+) KB")
        val memoryMatch = memoryRegex.find(entireOutput)
        val memoryUsageKB = memoryMatch?.groups?.get(1)?.value?.toLongOrNull() ?: 0

        println("entireOutput: $entireOutput")
        println("actualOutput: $actualOutput")
        println("MemoryKB Usage: ${memoryUsageKB}KB")

        // 출력 비교
        if (hasPresentationError(actualOutput, expectedOutput)) {
            return ExecutionResult(
                output = actualOutput,
                error = "",
                success = false,
                state = ProblemSubmitState.PRESENTATION_ERROR,
                timeUsage = timeUsage,
                memoryUsage = memoryUsageKB,
            )
        }

        if (actualOutput != expectedOutput) {
            return ExecutionResult(
                output = actualOutput,
                error = "",
                success = false,
                state = ProblemSubmitState.WRONG_ANSWER,
                timeUsage = timeUsage,
                memoryUsage = memoryUsageKB,
            )
        }

        return ExecutionResult(
            output = actualOutput,
            error = "",
            success = true,
            state = ProblemSubmitState.ACCEPTED,
            timeUsage = timeUsage,
            memoryUsage = memoryUsageKB,
        )
    }

    private fun getDockerImage(language: ProblemSubmitLanguage): String {
        return when (language) {
            ProblemSubmitLanguage.PYTHON -> "python:3.11"
            ProblemSubmitLanguage.JAVA -> "openjdk:17"
            ProblemSubmitLanguage.C -> "gcc"
            else -> throw CustomException(ProblemError.LANGUAGE_NOT_SUPPORTED)
        }
    }

    private fun processStream(stream: java.io.InputStream, buffer: StringBuilder) {
        try {
            stream.bufferedReader().use { reader ->
                while (!Thread.currentThread().isInterrupted) {
                    val line = reader.readLine() ?: break
                    buffer.append(line).append("\n")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun hasPresentationError(actual: String, expected: String): Boolean {
        val normalizedActual = actual.replace("\\s+".toRegex(), "")
        val normalizedExpected = expected.replace("\\s+".toRegex(), "")

        return normalizedActual == normalizedExpected && actual != expected
    }

//    private fun compile(sourceFile: File): ExecutionResult? {
//        return when (request.language) {
//            ProblemSubmitLanguage.JAVA -> compileJava(sourceFile)
//            ProblemSubmitLanguage.PYTHON -> checkPythonSyntax(sourceFile)
//            else -> null
//        }
//    }

    private fun checkPythonSyntax(sourceFile: File): ExecutionResult? {
        val process = ProcessBuilder("python3", "-m", "py_compile", sourceFile.absolutePath)
            .redirectErrorStream(true)
            .start()

        val output = process.inputStream.bufferedReader().use { it.readText() }
        val exitCode = process.waitFor()

        return if (exitCode != 0) {
            ExecutionResult(
                output = "",
                error = output,
                success = false,
                state = ProblemSubmitState.COMPILE_ERROR,
                compilationOutput = output
            )
        } else null
    }

//    private fun compileJava(sourceFile: File): ExecutionResult? {
//        val process = ProcessBuilder("javac", sourceFile.absolutePath)
//            .redirectErrorStream(true)
//            .start()
//
//        val output = process.inputStream.bufferedReader().use { it.readText() }
//        val exitCode = process.waitFor()
//
//        return if (exitCode != 0) {
//            ExecutionResult(
//                output = "",
//                error = output,
//                success = false,
//                state = ProblemSubmitState.COMPILE_ERROR,
//                compilationOutput = output
//            )
//        } else null
//    }


}
