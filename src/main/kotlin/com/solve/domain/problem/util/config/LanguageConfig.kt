package com.solve.domain.problem.util.config

import com.solve.domain.problem.domain.enums.ProblemSubmitLanguage
import java.io.File

data class LanguageConfig (
    val name: String,
    val fileName: (submitId: Long) -> String,
    val sourceDirectory: (submitId: Long, basePath: String) -> File,
    val executionTarget: (submitId: Long) -> String
) {
    companion object {
        val languageConfigs = mapOf(
            ProblemSubmitLanguage.PYTHON to LanguageConfig(
                name = "python",
                fileName = { submitId -> "$submitId.py" },
                sourceDirectory = { _, basePath -> File(basePath, "submits") },
                executionTarget = { submitId -> "$submitId.py" }
            ),
            ProblemSubmitLanguage.JAVA to LanguageConfig(
                name = "java",
                fileName = { _ -> "Main.java" },
                sourceDirectory = { submitId, basePath -> File(basePath, "submits/$submitId") },
                executionTarget = { submitId -> "$submitId/Main.java" }
            ),
            ProblemSubmitLanguage.C to LanguageConfig(
                name = "c",
                fileName = { submitId -> "$submitId.c" },
                sourceDirectory = { _, basePath -> File(basePath, "submits") },
                executionTarget = { submitId -> "$submitId.c" }
            )
        )
    }

}