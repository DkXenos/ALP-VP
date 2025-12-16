package com.jason.alp_vp.data.dto.post

data class PostResponseItem(
    val author: Author,
    val content: String,
    val createdAt: String,
    val id: Int,
    val image: Any,
    val userId: Int
)