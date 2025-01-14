package com.solve.domain.template.service.impl

import com.solve.domain.template.service.TemplateService
import com.solve.global.common.enums.ProgrammingLanguage
import org.springframework.stereotype.Service

@Service
class TemplateServiceImpl: TemplateService {
    override fun getTemplate(language: ProgrammingLanguage) = language.template
}