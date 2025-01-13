package com.solve.domain.problem.util.config

import com.solve.domain.problem.domain.enums.ProblemSubmitLanguage
import java.io.File

data class LanguageConfig (
    val name: String,
    val fileName: String,
    val getSourceDirectory: (submitId: Long, basePath: String) -> File,
    val getExecutionTarget: (submitId: Long) -> String
) {
    companion object {
        val LANGUAGE_CONFIGS = mapOf(
            ProblemSubmitLanguage.PYTHON to LanguageConfig(
                name = "python",
                fileName = "main.py",
                getSourceDirectory = { submitId, basePath -> File(basePath, "submits/$submitId") },
                getExecutionTarget = { submitId -> "$submitId/main.py" }
            ),
            ProblemSubmitLanguage.JAVA to LanguageConfig(
                name = "java",
                fileName = "Main.java",
                getSourceDirectory = { submitId, basePath -> File(basePath, "submits/$submitId") },
                getExecutionTarget = { submitId -> "$submitId/Main.java" }
            ),
            ProblemSubmitLanguage.C to LanguageConfig(
                name = "c",
                fileName = "main.c",
                getSourceDirectory = { submitId, basePath -> File(basePath, "submits/$submitId") },
                getExecutionTarget = { submitId -> "$submitId/main.c" }
            )
        )
    }

}