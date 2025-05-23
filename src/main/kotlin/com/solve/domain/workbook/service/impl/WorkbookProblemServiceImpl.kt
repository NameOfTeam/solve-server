package com.solve.domain.workbook.service.impl

import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.workbook.domain.entity.WorkbookProblem
import com.solve.domain.workbook.dto.request.AddWorkbookProblemRequest
import com.solve.domain.workbook.error.WorkbookError
import com.solve.domain.workbook.error.WorkbookProblemError
import com.solve.domain.workbook.repository.WorkbookProblemRepository
import com.solve.domain.workbook.repository.WorkbookRepository
import com.solve.domain.workbook.service.WorkbookProblemService
import com.solve.global.error.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorkbookProblemServiceImpl(
    private val workbookRepository: WorkbookRepository,
    private val problemRepository: ProblemRepository,
    private val workbookProblemRepository: WorkbookProblemRepository
) : WorkbookProblemService {
    @Transactional
    override fun addWorkbookProblem(workbookId: Long, request: AddWorkbookProblemRequest) {
        val workbook = workbookRepository.findByIdOrNull(workbookId)
            ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)
        val problem =
            problemRepository.findByIdOrNull(request.problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        workbookProblemRepository.save(
            WorkbookProblem(
                workbook = workbook,
                problem = problem
            )
        )
    }

    @Transactional
    override fun removeWorkbookProblem(workbookId: Long, problemId: Long) {
        val workbook = workbookRepository.findByIdOrNull(workbookId)
            ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)
        val problem = problemRepository.findByIdOrNull(problemId)
            ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val workbookProblem = workbookProblemRepository.findByWorkbookAndProblem(workbook, problem)
            ?: throw CustomException(WorkbookProblemError.WORKBOOK_PROBLEM_NOT_FOUND)

        workbookProblemRepository.delete(workbookProblem)
    }
}