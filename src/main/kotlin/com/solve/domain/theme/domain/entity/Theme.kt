package com.solve.domain.theme.domain.entity

import com.solve.global.common.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "themes")
class Theme(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "description", nullable = false)
    val description: String,

    @Column(name = "thumbnail", nullable = false)
    val thumbnail: String,

    @Column(name = "background", nullable = false)
    val background: String,

    @Column(name = "background_border", nullable = false)
    val backgroundBorder: String,

    @Column(name = "container", nullable = false)
    val container: String,

    @Column(name = "container_border", nullable = false)
    val containerBorder: String,

    @Column(name = "main", nullable = false)
    val main: String,

    @Column(name = "price", nullable = false)
    val price: Int,
) : BaseTimeEntity()