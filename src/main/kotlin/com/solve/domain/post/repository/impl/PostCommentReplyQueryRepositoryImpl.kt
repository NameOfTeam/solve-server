package com.solve.domain.post.repository.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.domain.entity.PostComment
import com.solve.domain.post.domain.entity.PostCommentReply
import com.solve.domain.post.domain.entity.QPostCommentReply
import com.solve.domain.post.repository.PostCommentReplyQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class PostCommentReplyQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : PostCommentReplyQueryRepository {
    private val reply = QPostCommentReply.postCommentReply

    @Transactional(readOnly = true)
    override fun getReplies(post: Post, comment: PostComment, pageable: Pageable): Page<PostCommentReply> {
        val baseQuery = queryFactory
            .selectFrom(reply)
            .where(
                reply.post.eq(post),
                reply.comment.eq(comment)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(reply._createdAt.asc())

        val replies = baseQuery.fetch()
        val totalCount = queryFactory
            .select(reply.count())
            .from(reply)
            .where(
                reply.post.eq(post),
                reply.comment.eq(comment)
            )
            .fetchOne() ?: 0L

        return PageImpl(replies, pageable, totalCount)
    }
}