package com.solve.domain.problem.util

import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.util.config.LanguageConfig
import com.solve.domain.submit.domain.entity.Submit
import com.solve.domain.submit.domain.enums.SubmitState
import com.solve.domain.submit.dto.request.SubmitRequest
import com.solve.global.config.file.FileProperties
import com.solve.global.error.CustomException
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class CodeExecutor(
    private val submit: Submit,
    private val request: SubmitRequest,
    private val fileProperties: FileProperties
) {
    data class ExecutionResult(
        val output: String,
        val error: String,
        val success: Boolean,
        val state: SubmitState? = null,
        val timeUsage: Long = 0,
        val compilationOutput: String? = null,
        val memoryUsage: Long = 0,
    )

    private val languageConfig: LanguageConfig = LanguageConfig.LANGUAGE_CONFIGS[request.language]
        ?: throw CustomException(ProblemError.LANGUAGE_NOT_SUPPORTED)

    private fun createSourceFile(): File {
        val directory = getSourceDirectory(submit.id!!, fileProperties.path).apply { if (!exists()) mkdirs() }
        val fileName = languageConfig.fileName
        return File(directory, fileName).apply {
            createNewFile()
            writeText(request.code)
        }
    }

    fun execute(input: String, timeLimit: Double, expectedOutput: String): ExecutionResult {
        val sourceFile = createSourceFile()

        compile(sourceFile)?.let { return it }

        val scriptPath = "/app/cmd/${languageConfig.name}_execute.sh"

        val command = listOf(
            "docker", "exec", "--privileged", "${languageConfig.name}-judge", "sh", "-c",
            "$scriptPath '${input.replace("'", "'\\''")}' ${languageConfig.getExecutionTarget(submit.id!!)}"
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
                state = SubmitState.TIME_LIMIT_EXCEEDED
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
                state = SubmitState.RUNTIME_ERROR
            )
        }

        val perfOutput = if (isPerfOutput) errorOutput else ""
        val entireOutput = output.toString().trim()

        // Perf 출력 이후의 내용 제거
        var actualOutput = entireOutput.substringBefore("Performance counter stats for").trim()
        actualOutput = actualOutput.replace(Regex("Memory Usage: .*"), "").trim()

        println(actualOutput)
        println(perfOutput)

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
                state = SubmitState.PRESENTATION_ERROR,
                timeUsage = timeUsage,
                memoryUsage = memoryUsage,
            )
        }

        if (actualOutput != expectedOutput) {
            return ExecutionResult(
                output = actualOutput,
                error = "",
                success = false,
                state = SubmitState.WRONG_ANSWER,
                timeUsage = timeUsage,
                memoryUsage = memoryUsage,
            )
        }

        return ExecutionResult(
            output = actualOutput,
            error = "",
            success = true,
            state = SubmitState.ACCEPTED,
            timeUsage = timeUsage,
            memoryUsage = memoryUsage,
        )
    }

    private fun hasPresentationError(actual: String, expected: String): Boolean {
        val normalizedActual = actual.replace("\\s+".toRegex(), "")
        val normalizedExpected = expected.replace("\\s+".toRegex(), "")

        return normalizedActual == normalizedExpected && actual != expected
    }

    private fun compile(sourceFile: File): ExecutionResult? {
        val compileCmd = languageConfig.compileCmd(sourceFile.absolutePath)
        val process = ProcessBuilder(compileCmd)
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().use { it.readText() }
        val exitCode = process.waitFor()
        return if (exitCode != 0) {
            ExecutionResult(
                output = "",
                error = output,
                success = false,
                state = SubmitState.COMPILE_ERROR,
                compilationOutput = output
            )
        } else null
    }

    private fun getSourceDirectory(submitId: Long, path: String): File {
        return File(path, "submits/$submitId")
    }
}
