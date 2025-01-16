package com.solve.domain.post.repository.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.domain.entity.PostComment
import com.solve.domain.post.domain.entity.QPostComment
import com.solve.domain.post.repository.PostCommentQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class PostCommentQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : PostCommentQueryRepository {
    private val postComment = QPostComment.postComment

    @Transactional(readOnly = true)
    override fun getComments(
        post: Post,
        cursorId: Long?,
        size: Int
    ): List<PostComment> {
        return queryFactory
            .selectFrom(postComment)
            .where(
                postComment.post.eq(post),
                cursorIdCondition(cursorId)
            )
            .orderBy(postComment._createdAt.desc(), postComment.id.desc())
            .limit(size.toLong())
            .fetch() ?: emptyList()
    }

    private fun cursorIdCondition(cursorId: Long?): BooleanExpression? {
        return if (cursorId != null && cursorId > 0) {
            postComment.id.lt(cursorId)
        } else {
            null
        }
    }
}