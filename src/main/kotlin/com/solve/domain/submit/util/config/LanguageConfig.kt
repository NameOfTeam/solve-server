package com.solve.domain.submit.util.config

import com.solve.global.common.enums.ProgrammingLanguage

data class LanguageConfig(
    val name: String,
    val fileName: String,
    val compileCmd: (sourceFile: String) -> List<String>
) {
    fun getExecutionTarget(submitId: Long): String {
        return "$submitId/${fileName}"
    }

    companion object {
        val LANGUAGE_CONFIGS = mapOf(
            ProgrammingLanguage.PYTHON to LanguageConfig(
                name = "python",
                fileName = "main.py",
                compileCmd = { sourceFile -> listOf("python3", "-m", "py_compile", sourceFile) },
            ),
            ProgrammingLanguage.JAVA to LanguageConfig(
                name = "java",
                fileName = "Main.java",
                compileCmd = { sourceFile -> listOf("javac", sourceFile) },
            ),
            ProgrammingLanguage.C to LanguageConfig(
                name = "c",
                fileName = "main.c",
                compileCmd = { sourceFile -> listOf("gcc", sourceFile, "-o", sourceFile.replace(".c", "")) },
            ),
            ProgrammingLanguage.NODE_JS to LanguageConfig(
                name = "node",
                fileName = "main.js",
                compileCmd = { sourceFile -> listOf("gcc", sourceFile, "-o", sourceFile.replace(".c", "")) }
            )
        )
    }

}