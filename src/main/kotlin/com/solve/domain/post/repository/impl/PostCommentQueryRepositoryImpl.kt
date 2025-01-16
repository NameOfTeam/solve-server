package com.solve.domain.post.repository.impl

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
    override fun getComments(post: Post, pageable: Pageable): Page<PostComment> {
        val baseQuery = queryFactory
            .selectFrom(postComment)
            .where(postComment.post.eq(post))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(postComment._createdAt.desc())

        val comments = baseQuery.fetch()
        val totalCount = queryFactory
            .select(postComment.count())
            .from(postComment)
            .where(postComment.post.eq(post))
            .fetchOne() ?: 0L

        return PageImpl(comments, pageable, totalCount)
    }
}