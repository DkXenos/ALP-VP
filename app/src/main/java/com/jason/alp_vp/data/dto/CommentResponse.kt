package com.jason.alp_vp.data.dto

data class CommentResponse(
    val commentVotes: List<CommentVote>,
    val content: String,
    val created_at: String,
    val id: Int,
    val post_id: Int
)