package com.jason.alp_vp.ui.model

import java.time.Instant
import com.jason.alp_vp.ui.model.CommentVote

data class Comment(
    val id: Int,
    val postId: Int,
    val content: String,
    val createdAt: Instant = Instant.now(),
    val commentVotes: List<CommentVote> = emptyList<CommentVote>()
)
