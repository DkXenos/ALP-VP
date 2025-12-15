package com.jason.alp_vp.data.dto.comment

data class CommentResponseItem(
    val author: Author,
    val content: String,
    val createdAt: String,
    val id: Int,
    val postId: Int,
    val votes: List<Vote>
)