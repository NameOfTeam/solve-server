package com.solve.domain.problem.repository

import com.solve.domain.problem.domain.entity.ProblemIdea
import com.solve.domain.problem.domain.entity.ProblemIdeaComment
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemIdeaCommentRepository : JpaRepository<ProblemIdeaComment, Long> {
    fun findAllByIdeaId(ideaId: Long): List<ProblemIdeaComment>
    fun findByIdAndIdea(id: Long, idea: ProblemIdea): ProblemIdeaComment?
}