package com.jason.alp_vp.ui.model

import java.time.Instant

data class Vote(
    val id: Int,
    val commentId: Int,
    val userId: Int,
    val voteType: String, // 'upvote' or 'downvote'
    val createdAt: Instant = Instant.now()
)
