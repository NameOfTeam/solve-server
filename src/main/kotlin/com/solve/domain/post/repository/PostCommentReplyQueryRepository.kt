package com.solve.domain.post.repository

import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.domain.entity.PostComment
import com.solve.domain.post.domain.entity.PostCommentReply
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostCommentReplyQueryRepository {
    fun getReplies(post: Post, comment: PostComment, pageable: Pageable): Page<PostCommentReply>
}