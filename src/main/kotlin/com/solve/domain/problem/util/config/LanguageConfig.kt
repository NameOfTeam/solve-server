package com.solve.domain.problem.util.config

import com.solve.domain.problem.domain.enums.ProblemSubmitLanguage
import java.io.File

data class LanguageConfig (
    val name: String,
    val fileName: (submitId: Long) -> String,
    val sourceDirectory: (submitId: Long, basePath: String) -> File,
    val executionTarget: (submitId: Long) -> String,
    val compileCmd: (sourceFile: String) -> List<String>
) {
    companion object {
        val languageConfigs = mapOf(
            ProblemSubmitLanguage.PYTHON to LanguageConfig(
                name = "python",
                fileName = { submitId -> "$submitId.py" },
                sourceDirectory = { _, basePath -> File(basePath, "submits") },
                executionTarget = { submitId -> "$submitId.py" },
                compileCmd = { sourceFile -> listOf("python3", "-m", "py_compile", sourceFile) }
            ),
            ProblemSubmitLanguage.JAVA to LanguageConfig(
                name = "java",
                fileName = { _ -> "Main.java" },
                sourceDirectory = { submitId, basePath -> File(basePath, "submits/$submitId") },
                executionTarget = { submitId -> "$submitId/Main.java" },
                compileCmd = { sourceFile -> listOf("javac", sourceFile) }
            ),
            ProblemSubmitLanguage.C to LanguageConfig(
                name = "c",
                fileName = { submitId -> "$submitId.c" },
                sourceDirectory = { _, basePath -> File(basePath, "submits") },
                executionTarget = { submitId -> "$submitId.c" },
                compileCmd = { sourceFile -> listOf("gcc", sourceFile, "-o", sourceFile.replace(".c", "")) }
            )
        )
    }

}