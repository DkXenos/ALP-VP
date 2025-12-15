package com.jason.alp_vp.data.dto

data class PostResponse(
    val comments: List<Any>,
    val content: String,
    val created_at: String,
    val id: Int,
    val image: Any,
    val user_id: Int
)