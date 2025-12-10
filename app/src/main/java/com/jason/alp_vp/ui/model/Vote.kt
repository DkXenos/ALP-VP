package com.jason.alp_vp.ui.model

data class Vote(
    val id: String,
    val voteType: String, // "upvote" or "downvote"
    val createdAt: Long
)