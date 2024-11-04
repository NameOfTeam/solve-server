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

class CodeExecutor(
    private val submit: ProblemSubmit,
    private val request: ProblemSubmitRequest,
    private val fileProperties: FileProperties
) {
    private var memoryUsage = 0L  // MB 단위
    private var isMemoryLimitExceeded = false
    private var isTimeLimitExceeded = false
    private var shouldStopMonitoring = false

    data class ExecutionResult(
        val output: String,
        val error: String,
        val success: Boolean,
        val state: ProblemSubmitState? = null,
        val timeUsage: Long = 0,
        val compilationOutput: String? = null
    )

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
        else -> throw CustomException(ProblemError.LANGUAGE_NOT_SUPPORTED)
    }

    private fun compile(sourceFile: File): ExecutionResult? {
        return when (request.language) {
            ProblemSubmitLanguage.JAVA -> compileJava(sourceFile)
            ProblemSubmitLanguage.PYTHON -> checkPythonSyntax(sourceFile)
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

    fun execute(input: String, timeLimit: Double, expectedOutput: String): ExecutionResult {
        val sourceFile = createSourceFile()

        // 컴파일 체크
        compile(sourceFile)?.let { return it }

        val process = when (request.language) {
            ProblemSubmitLanguage.PYTHON -> ProcessBuilder("python3", sourceFile.absolutePath)
            ProblemSubmitLanguage.JAVA -> ProcessBuilder(
                "java",
                "-cp",
                sourceFile.parent,
                sourceFile.nameWithoutExtension
            )

            else -> throw CustomException(ProblemError.LANGUAGE_NOT_SUPPORTED)
        }.start()

        val pid = process.pid()
        val output = StringBuilder()
        val error = StringBuilder()
        val startTime = System.nanoTime()

        // 메모리 모니터링 스레드
        val memoryThread = thread { monitorMemory(pid, submit.problem.memoryLimit) }

        // 출력 스트림 처리 스레드
        val outputThread = thread { processStream(process.inputStream, output) }
        val errorThread = thread { processStream(process.errorStream, error) }

        // 입력 처리 스레드
        thread {
            try {
                process.outputStream.use {
                    it.write(input.toByteArray())
                    it.flush()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        // 시간 제한 체크
        val timeLimitMillis = (timeLimit * 1000).toLong()
        val finishedInTime = process.waitFor(timeLimitMillis, TimeUnit.MILLISECONDS)
        val timeUsage = ((System.nanoTime() - startTime) / 1_000_000).toLong()

        if (!finishedInTime) {
            isTimeLimitExceeded = true
            shouldStopMonitoring = true  // 메모리 모니터링 중지 신호
            process.destroyForcibly()
            process.waitFor(1, TimeUnit.SECONDS)  // 프로세스가 완전히 종료되길 기다림

            // 스레드들 정리
            memoryThread.interrupt()
            outputThread.interrupt()
            errorThread.interrupt()

            return ExecutionResult(
                output = "",
                error = "",
                success = false,
                state = ProblemSubmitState.TIME_LIMIT_EXCEEDED,
                timeUsage = timeUsage
            )
        }

        // 메모리 모니터링 중지 신호
        shouldStopMonitoring = true
        memoryThread.join(1000)
        outputThread.join(1000)
        errorThread.join(1000)

        // 시간 초과가 아닐 때만 메모리 초과 체크
        if (isMemoryLimitExceeded) {
            return ExecutionResult(
                output = "",
                error = "",
                success = false,
                state = ProblemSubmitState.MEMORY_LIMIT_EXCEEDED,
                timeUsage = timeUsage
            )
        }

        val errorOutput = error.toString().trim()
        if (errorOutput.isNotEmpty()) {
            return ExecutionResult(
                output = "",
                error = errorOutput,
                success = false,
                state = ProblemSubmitState.RUNTIME_ERROR,
                timeUsage = timeUsage
            )
        }

        val actualOutput = output.toString().trim()

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

    private fun processStream(stream: java.io.InputStream, buffer: StringBuilder) {
        try {
            stream.bufferedReader().use { reader ->
                while (!Thread.currentThread().isInterrupted) {
                    val line = reader.readLine() ?: break
                    buffer.append(line).append("\n")
                }
            }
        } catch (e: IOException) {
            // 스트림이 닫힌 경우 등의 예외 처리
            e.printStackTrace()
        }
    }

    private fun monitorMemory(pid: Long, memoryLimit: Long) {
        while (!Thread.currentThread().isInterrupted && !shouldStopMonitoring) {
            try {
                val memoryMB = getMemoryUsage(pid)
                if (memoryMB > memoryUsage) {
                    memoryUsage = memoryMB
                    submit.memoryUsage = memoryMB
                }
                if (memoryMB > memoryLimit) {
                    isMemoryLimitExceeded = true
                    break
                }
                Thread.sleep(50)
            } catch (e: Exception) {
                break
            }
        }
    }

    private fun getMemoryUsage(pid: Long): Long {
        return try {
            // ps 명령어로 얻는 RSS 값은 KB 단위
            val process = ProcessBuilder("sh", "-c", "ps -o rss= -p $pid")
                .redirectErrorStream(true)
                .start()

            process.inputStream.bufferedReader().useLines { lines ->
                lines.firstOrNull { it.isNotBlank() }?.trim()?.toLong()?.let { rssKB ->
                    // KB를 MB로 변환 (1024로 나눔)
                    rssKB / 1024
                } ?: -1
            }.also {
                process.waitFor(100, TimeUnit.MILLISECONDS)
                process.destroyForcibly()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    private fun hasPresentationError(actual: String, expected: String): Boolean {
        // 공백과 줄바꿈을 모두 제거하고 비교
        val normalizedActual = actual.replace("\\s+".toRegex(), "")
        val normalizedExpected = expected.replace("\\s+".toRegex(), "")

        // 내용은 같지만 형식이 다른 경우
        return normalizedActual == normalizedExpected && actual != expected
    }

    private fun cleanupResources() {
        try {
            // 임시 파일 삭제 등의 정리 작업
            val directory = File(fileProperties.path, "submits")
            val sourceFile = File(directory, "${submit.id}.${getFileExtension()}")
            if (sourceFile.exists()) {
                sourceFile.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}