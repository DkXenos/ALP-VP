package com.jason.alp_vp.ui.model

import java.time.Instant

data class Post(
    val id: Int,
    val userId: Int,
    val content: String,
    val image: String? = null,
    val createdAt: Instant = Instant.now(),
    val comments: List<Comment> = emptyList()
)
