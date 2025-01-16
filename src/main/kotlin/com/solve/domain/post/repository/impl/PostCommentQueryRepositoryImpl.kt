package com.solve.domain.post.repository.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.domain.entity.PostComment
import com.solve.domain.post.domain.entity.QPostComment
import com.solve.domain.post.repository.PostCommentQueryRepository
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
        cursor: PostComment?,
        size: Int
    ): List<PostComment> {
        return queryFactory
            .selectFrom(postComment)
            .where(
                postComment.post.eq(post),
                cursorIdCondition(cursor)
            )
            .orderBy(postComment._createdAt.desc(), postComment.id.desc())
            .limit(size.toLong())
            .fetch() ?: emptyList()
    }

    private fun cursorIdCondition(cursor: PostComment?): BooleanExpression? {
        return if (cursor != null) {
            postComment.id.lt(cursor.id!!)
        } else {
            null
        }
    }
}