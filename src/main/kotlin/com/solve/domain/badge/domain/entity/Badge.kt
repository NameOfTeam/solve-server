package com.solve.domain.badge.domain.entity

import com.solve.global.common.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "badges")
class Badge(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "image_url", nullable = false)
    var imageUrl: String,

    @Column(name = "condition", nullable = false)
    var condition: String
): BaseTimeEntity()