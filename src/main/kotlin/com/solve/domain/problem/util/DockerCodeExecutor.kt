package com.solve.domain.problem.util

import com.solve.domain.problem.domain.entity.ProblemSubmit
import com.solve.domain.problem.domain.enums.ProblemSubmitLanguage
import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.problem.dto.request.ProblemSubmitRequest
import com.solve.domain.problem.error.ProblemError
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
        val compilationOutput: String? = null
    )

    private val pythonContainerName = "problem-judge-container"
    private val javaContainerName = "java-problem-judge-container"

    fun initializePythonContainer() {
        initializeContainer(pythonContainerName, "python:3.11", listOf("apt-get update && apt-get install -y time"))
    }

    fun initializeJavaContainer() {
        initializeContainer(
            containerName = "java-problem-judge-container",
            imageName = "openjdk:17",
            setupCommands = listOf("apk update", "apk add --no-cache time")
        )
    }

    private fun initializeContainer(containerName: String, imageName: String, setupCommands: List<String>) {
        // 기존 컨테이너 제거
        val removeCommand = listOf("docker", "rm", "-f", containerName)
        println("Removing existing container with command: $removeCommand")
        ProcessBuilder(removeCommand).start().waitFor()

        // 새 컨테이너 생성
        val setupScript = setupCommands.joinToString(" && ")
        val startCommand = listOf(
            "docker", "run", "--name", containerName, "-d",
            "-v", "${fileProperties.path}:/app/submit",
            imageName, "sh", "-c",
            "$setupScript && tail -f /dev/null"
        )
        println("Starting container with command: $startCommand")
        ProcessBuilder(startCommand).start().waitFor()
    }

    private fun createSourceFile(): File {
        val directory = File(fileProperties.path, "submits").apply {
            if (!exists()) mkdirs()
        }
        val restoredCode = request.code.replace("\\n", "\n").replace("\\\"", "\"")

        return File(directory, "${submit.id}.${getFileExtension()}").apply {
            createNewFile()
            writeText(restoredCode)
        }
    }

    private fun getFileExtension() = when (request.language) {
        ProblemSubmitLanguage.PYTHON -> "py"
        ProblemSubmitLanguage.JAVA -> "java"
        ProblemSubmitLanguage.C -> "c"
        else -> throw CustomException(ProblemError.LANGUAGE_NOT_SUPPORTED)
    }

    fun execute(input: String, timeLimit: Double, expectedOutput: String): ExecutionResult {
        val sourceFile = createSourceFile()
        val command = listOf(
            "docker", "exec", "--privileged", pythonContainerName, "perf", "stat",
            "python3", "/app/submit/submits/${sourceFile.name}"
        )

        println("Executing command: $command")

        val processBuilder = ProcessBuilder(command)
        val process = processBuilder.start()
        val output = StringBuilder()
        val error = StringBuilder()

        // 스트림 처리
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

        val perfOutput = if (isPerfOutput) errorOutput else output.toString()
        val timeRegex = Regex("(\\d+\\.\\d+) seconds time elapsed")
        val timeMatch = timeRegex.find(perfOutput)

        val memoryRegex = Regex("(\\d+) page-faults")

        val memoryMatch = memoryRegex.find(errorOutput)

        val timeUsage = timeMatch?.groups?.get(1)?.value?.toDoubleOrNull()?.let {
            (it * 1000).toInt().toLong()
        } ?: -1


        val actualOutput = output.toString().trim()

        println("actualOutput: $actualOutput")

        // 출력 비교
        if (hasPresentationError(actualOutput, expectedOutput)) {
            return ExecutionResult(
                output = actualOutput,
                error = "",
                success = false,
                state = ProblemSubmitState.PRESENTATION_ERROR,
                timeUsage = timeUsage
            )
        }

        if (actualOutput != expectedOutput) {
            return ExecutionResult(
                output = actualOutput,
                error = "",
                success = false,
                state = ProblemSubmitState.WRONG_ANSWER,
                timeUsage = timeUsage
            )
        }

        return ExecutionResult(
            output = actualOutput,
            error = "",
            success = true,
            state = ProblemSubmitState.ACCEPTED,
            timeUsage = timeUsage
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

    private fun getExecutionCommand(sourceFile: File): String {
        return when (request.language) {
            ProblemSubmitLanguage.PYTHON -> "python3 /app/submit/submits/${sourceFile.name}"
            ProblemSubmitLanguage.JAVA -> "javac /app/submit/submits/${sourceFile.name} && java -cp . /app/submit/submits/${sourceFile.nameWithoutExtension}"
            ProblemSubmitLanguage.C -> "gcc ${sourceFile.name} -o a.out && ./a.out"
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

    fun test() {
        val processBuilder = ProcessBuilder(
            "sh", "-c", "docker ps"
        )

        val process = processBuilder.start()
        val output = StringBuilder()

        process.inputStream.bufferedReader().useLines { lines ->
            lines.forEach { output.append(it).append("\n") }
        }

        val error = StringBuilder()

        process.errorStream.bufferedReader().useLines { lines ->
            lines.forEach { error.append(it).append("\n") }
        }

        println("TEST Error Output: $error")

        println("Test Output: $output")
    }

}
