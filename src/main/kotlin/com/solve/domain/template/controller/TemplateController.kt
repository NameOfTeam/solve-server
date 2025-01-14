package com.solve.domain.template.controller

import com.solve.domain.template.service.TemplateService
import com.solve.global.common.dto.BaseResponse
import com.solve.global.common.enums.ProgrammingLanguage
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "템플릿", description = "Template")
@RestController
@RequestMapping("/templates")
class TemplateController(
    private val templateService: TemplateService
) {
    @Operation(summary = "템플릿 조회", description = "템플릿을 조회합니다.")
    @GetMapping("/{language}")
    fun getTemplate(@PathVariable language: ProgrammingLanguage) = BaseResponse.of(templateService.getTemplate(language))
}