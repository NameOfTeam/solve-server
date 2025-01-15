package com.solve.domain.problem.util

import com.solve.domain.problem.util.config.LanguageConfig
import com.solve.global.common.enums.ProgrammingLanguage
import com.solve.global.config.file.FileProperties
import java.io.BufferedWriter
import java.io.File

class CodeRunner(
    private val language: ProgrammingLanguage,
    private val fileProperties: FileProperties
) {
    private var process: Process? = null
    private var stdin: BufferedWriter? = null
    private var isRunning = false
    private val languageConfig = LanguageConfig.LANGUAGE_CONFIGS[language]
        ?: throw IllegalArgumentException("Unsupported language: ${language.name}")

    fun execute(runId: String, code: String): Process {
        val workDir = getSourceDirectory(runId)
        println(runId)
        workDir.mkdirs()

        val sourceFile = File(workDir, languageConfig.fileName)
        sourceFile.writeText(code)

        val containerName = languageConfig.name + "-" + "run"
        val containerWorkDir = "/app/runs/${runId}"

//        ProcessBuilder("docker", "exec", containerName, "mkdir", "-p", containerWorkDir)
//            .start().waitFor()
//
//        // 소스 파일을 컨테이너에 복사
//        ProcessBuilder("docker", "cp", sourceFile.absolutePath, "$containerName:$containerWorkDir/")
//            .start().waitFor()

        // 실행 명령어 생성
        val execCommand = createExecutionCommand(containerName, containerWorkDir)

        println(execCommand)

        process = ProcessBuilder(execCommand)
            .redirectErrorStream(true)
            .start()

        stdin = process?.outputStream?.bufferedWriter()
        isRunning = true

        return process!!
    }

    private fun createExecutionCommand(containerName: String, workDir: String): List<String> {
        return when (language) {
            ProgrammingLanguage.PYTHON -> listOf(
                "docker", "exec", "-w", workDir, "-i", containerName,
                "python3", "-u", languageConfig.fileName
            )
            ProgrammingLanguage.JAVA -> listOf(
                "docker", "exec", "-w", workDir, "-i", containerName,
                "java", "Main"
            )
            ProgrammingLanguage.C -> listOf(
                "docker", "exec", "-w", workDir, "-i", containerName,
                "./${languageConfig.fileName.replace(".c", "")}"
            )
            ProgrammingLanguage.NODE_JS -> listOf(
                "docker", "exec", "-w", workDir, "-i", containerName,
                "node", languageConfig.fileName
            )

            ProgrammingLanguage.KOTLIN -> TODO()
            ProgrammingLanguage.CPP -> TODO()
            ProgrammingLanguage.CSHARP -> TODO()
            ProgrammingLanguage.SWIFT -> TODO()
        }
    }

    fun sendInput(input: String) {
        stdin?.apply {
            write(input)
            newLine()
            flush()
        }
    }

    fun stop() {
        isRunning = false
        stdin?.close()
        process?.destroyForcibly()
    }

    private fun getSourceDirectory(runId: String): File {
        return File(fileProperties.path, "runs/$runId")
    }
}