package com.solve.domain.problem.util

import com.solve.domain.problem.domain.entity.ProblemSubmit
import com.solve.global.common.enums.ProgrammingLanguage
import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.problem.dto.request.ProblemSubmitRequest
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.util.config.LanguageConfig
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

    private val languageConfig: LanguageConfig = LanguageConfig.LANGUAGE_CONFIGS[request.language]
        ?: throw CustomException(ProblemError.LANGUAGE_NOT_SUPPORTED)

    private fun createSourceFile(): File {
        val directory = languageConfig.getSourceDirectory(submit.id!!, fileProperties.path).apply { if (!exists()) mkdirs() }
        val fileName = languageConfig.fileName
        return File(directory, fileName).apply {
            createNewFile()
            writeText(preprocessCode(request.code))
        }
    }

    private fun preprocessCode(code: String): String {
        return code.replace("\\n", "\n").replace("\\\"", "\"")
    }

    fun execute(input: String, timeLimit: Double, expectedOutput: String): ExecutionResult {
        val sourceFile = createSourceFile()

        compile(sourceFile)?.let { return it }

        val scriptPath = "/app/cmd/${languageConfig.name}_execute.sh"

        val command = listOf(
            "docker", "exec", "--privileged", "${languageConfig.name}-judge", "sh", "-c",
            "$scriptPath '${input.replace("'", "'\\''")}' ${languageConfig.getExecutionTarget(submit.id!!)}"
        )

//        println("Executing command: $command")

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
//            println("Error Output: $errorOutput")
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

//        println(actualOutput)
//        println(perfOutput)

        val timeUsage = Regex("(\\d+\\.\\d+) seconds time elapsed")
            .find(perfOutput)?.groups?.get(1)?.value?.toDoubleOrNull()?.let {
                (it * 1000).toInt().toLong()
            } ?: -1

        val memoryUsage = Regex("Memory Usage: (\\d+) KB")
            .find(entireOutput)?.groups?.get(1)?.value?.toLongOrNull() ?: 0

        if (hasPresentationError(actualOutput, expectedOutput)) {
            return ExecutionResult(
                output = actualOutput,
                error = "",
                success = false,
                state = ProblemSubmitState.PRESENTATION_ERROR,
                timeUsage = timeUsage,
                memoryUsage = memoryUsage,
            )
        }

        if (actualOutput != expectedOutput) {
            return ExecutionResult(
                output = actualOutput,
                error = "",
                success = false,
                state = ProblemSubmitState.WRONG_ANSWER,
                timeUsage = timeUsage,
                memoryUsage = memoryUsage,
            )
        }

        return ExecutionResult(
            output = actualOutput,
            error = "",
            success = true,
            state = ProblemSubmitState.ACCEPTED,
            timeUsage = timeUsage,
            memoryUsage = memoryUsage,
        )
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

    private fun compile(sourceFile: File): ExecutionResult? {
        return when (request.language) {
            ProgrammingLanguage.JAVA -> compileJava(sourceFile)
            ProgrammingLanguage.PYTHON -> checkPythonSyntax(sourceFile)
            else -> null
        }
    }

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

    private fun compileJava(sourceFile: File): ExecutionResult? {
        val process = ProcessBuilder("javac", sourceFile.absolutePath)
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
}
