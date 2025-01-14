package com.solve.domain.template.service

import com.solve.global.common.enums.ProgrammingLanguage

interface TemplateService {
    fun getTemplate(language: ProgrammingLanguage): String
}