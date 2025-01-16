package com.solve.domain.post.repository.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.domain.entity.PostComment
import com.solve.domain.post.domain.entity.PostCommentReply
import com.solve.domain.post.domain.entity.QPostCommentReply
import com.solve.domain.post.repository.PostCommentReplyQueryRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class PostCommentReplyQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : PostCommentReplyQueryRepository {
    private val reply = QPostCommentReply.postCommentReply

    @Transactional(readOnly = true)
    override fun getReplies(
        post: Post,
        comment: PostComment,
        cursor: PostCommentReply?,
        size: Int
    ): List<PostCommentReply> {
        return queryFactory
            .selectFrom(reply)
            .where(
                reply.post.eq(post),
                reply.comment.eq(comment),
                cursorIdCondition(cursor)
            )
            .orderBy(reply._createdAt.asc())
            .limit(size.toLong())
            .fetch() ?: emptyList()
    }

    private fun cursorIdCondition(cursor: PostCommentReply?): BooleanExpression? {
        return if (cursor != null) {
            reply.id.gt(cursor.id)
        } else {
            null
        }
    }
}