package com.jason.alp_vp.data.dto.vote

data class VoteResponseItem(
    val commentId: Int,
    val createdAt: String,
    val id: Int,
    val userId: Int,
    val voteType: String
)