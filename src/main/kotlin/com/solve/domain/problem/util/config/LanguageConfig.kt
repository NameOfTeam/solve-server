package com.solve.domain.problem.util.config

import com.solve.global.common.enums.ProgrammingLanguage
import java.io.File

data class LanguageConfig (
    val name: String,
    val fileName: String,
) {
    fun getExecutionTarget(submitId: Long): String {
        return "$submitId/${fileName}"
    }

    companion object {
        val LANGUAGE_CONFIGS = mapOf(
            ProgrammingLanguage.PYTHON to LanguageConfig(
                name = "python",
                fileName = "main.py",
            ),
            ProgrammingLanguage.JAVA to LanguageConfig(
                name = "java",
                fileName = "Main.java",
            ),
            ProgrammingLanguage.C to LanguageConfig(
                name = "c",
                fileName = "main.c",
            ),
            ProgrammingLanguage.NODE_JS to LanguageConfig(
                name = "node",
                fileName = "main.js",
            )
        )
    }

}