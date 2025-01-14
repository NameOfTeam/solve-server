package com.solve.domain.post.repository

import com.solve.domain.post.domain.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long> {

}