package com.jason.alp_vp.model

data class ForumPost(
    val id: String,
    val authorName: String,
    val authorAvatar: String,
    val content: String,
    val upvotes: Int,
    val timestamp: String
)

