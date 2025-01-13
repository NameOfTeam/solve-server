package com.solve.domain.post.repository.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.domain.entity.QPost
import com.solve.domain.post.domain.enums.PostCategory
import com.solve.domain.post.repository.PostQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class PostQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): PostQueryRepository {
    private val post = QPost.post

    @Transactional(readOnly = true)
    override fun searchPost(query: String, category: PostCategory?, pageable: Pageable): Page<Post> {
        val baseQuery = queryFactory
            .selectFrom(post)
            .distinct()
            .where(
                containsSearchKeyword(query),
                filterByCategory(category)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        baseQuery.orderBy(post._createdAt.desc())

        val posts = baseQuery.fetch()
        val totalCount = queryFactory
            .select(post.count())
            .from(post)
            .where(
                containsSearchKeyword(query),
                filterByCategory(category)
            )
            .fetchOne() ?: 0L

        return PageImpl(posts, pageable, totalCount)
    }

    private fun containsSearchKeyword(query: String): BooleanExpression? =
        if (query.isNotBlank()) {
            post.title.containsIgnoreCase(query)
                .or(post.content.containsIgnoreCase(query))
        } else null

    private fun filterByCategory(category: PostCategory?): BooleanExpression? =
        category?.let { post.category.eq(it) }
}