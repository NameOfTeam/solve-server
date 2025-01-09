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

    private val containerName = "problem-judge-container"

    fun initializeContainer() {
        val startCommand = listOf(
            "docker", "run", "--name", containerName, "-d",
            "-v", "${fileProperties.path}/submits:/app", // 수정: submits 디렉터리만 마운트
            "python:3.11", "tail", "-f", "/dev/null"
        )
        println("Starting container with command: $startCommand")
        ProcessBuilder(startCommand).start().waitFor()
    }

    private fun createSourceFile(): File {
        val directory = File(fileProperties.path, "submits").apply {
            if (!exists()) mkdirs()
        }
        return File(directory, "${submit.id}.${getFileExtension()}").apply {
            createNewFile()
            writeText(request.code)
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
        val dockerImage = getDockerImage(request.language)

        println("Created source file at: ${sourceFile.absolutePath}")
        println("Source file exists: ${sourceFile.exists()}")

        // 실행 명령어 디버깅용 로그 추가
        val command = listOf(
            "docker", "exec", containerName, "sh", "-c",
            "cd /app/submits && ${getExecutionCommand(sourceFile)}"
        )
        println("Executing in container with command: $command")

        // Docker 명령어 실행 전 권한 확인
        println("Current directory permissions: ${sourceFile.parent}")
        println("Source file exists: ${sourceFile.exists()}")

        val processBuilder = ProcessBuilder(command)
        val process = processBuilder.start()
        val output = StringBuilder()
        val error = StringBuilder()

        val startTime = System.nanoTime()

        // 스트림 읽기 스레드
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

        // 프로세스 실행 시간 제한
        val timeLimitMillis = (timeLimit * 1000).toLong()
        val finishedInTime = process.waitFor(timeLimitMillis, TimeUnit.MILLISECONDS)
        val timeUsage = ((System.nanoTime() - startTime) / 1_000_000)

        // 시간 초과 시 프로세스 종료
        if (!finishedInTime) {
            process.destroyForcibly()
            process.waitFor(1, TimeUnit.SECONDS)

            outputThread.interrupt()
            errorThread.interrupt()

            return ExecutionResult(
                output = "",
                error = "Time Limit Exceeded",
                success = false,
                state = ProblemSubmitState.TIME_LIMIT_EXCEEDED,
                timeUsage = timeUsage
            )
        }

        // 스트림 읽기 스레드 종료 대기
        outputThread.join(1000)
        errorThread.join(1000)

        // 에러 스트림 검사
        val errorOutput = error.toString().trim()
        if (errorOutput.isNotEmpty()) {
            println("Docker Error Output: $errorOutput")
            return ExecutionResult(
                output = "",
                error = errorOutput,
                success = false,
                state = ProblemSubmitState.RUNTIME_ERROR,
                timeUsage = timeUsage
            )
        }

        // 출력 스트림 검사
        val actualOutput = output.toString().trim()
        println("Docker Actual Output: $actualOutput")

// Docker 실행 결과 상세 로깅
        println("Process exit code: ${process.exitValue()}")
        println("Error stream: $errorOutput")
        println("Output stream: $actualOutput")

        // 결과 비교
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
            ProblemSubmitLanguage.PYTHON -> "python3 ${sourceFile.name}"
            ProblemSubmitLanguage.JAVA -> "javac ${sourceFile.name} && java -cp . ${sourceFile.nameWithoutExtension}"
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
