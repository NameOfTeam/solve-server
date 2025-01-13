package com.solve.domain.problem.util.config

import com.solve.global.common.enums.ProgrammingLanguage
import java.io.File

data class LanguageConfig (
    val name: String,
    val fileName: String,
    val getSourceDirectory: (submitId: Long, basePath: String) -> File,
    val getExecutionTarget: (submitId: Long) -> String
) {
    companion object {
        val LANGUAGE_CONFIGS = mapOf(
            ProgrammingLanguage.PYTHON to LanguageConfig(
                name = "python",
                fileName = "main.py",
                getSourceDirectory = { submitId, basePath -> File(basePath, "submits/$submitId") },
                getExecutionTarget = { submitId -> "$submitId/main.py" }
            ),
            ProgrammingLanguage.JAVA to LanguageConfig(
                name = "java",
                fileName = "Main.java",
                getSourceDirectory = { submitId, basePath -> File(basePath, "submits/$submitId") },
                getExecutionTarget = { submitId -> "$submitId/Main.java" }
            ),
            ProgrammingLanguage.C to LanguageConfig(
                name = "c",
                fileName = "main.c",
                getSourceDirectory = { submitId, basePath -> File(basePath, "submits/$submitId") },
                getExecutionTarget = { submitId -> "$submitId/main.c" }
            )
        )
    }

}