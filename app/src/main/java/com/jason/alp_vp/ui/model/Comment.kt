package com.jason.alp_vp.ui.model

import java.time.Instant

data class Comment(
    val id: Int,
    val postId: Int,
    val content: String,
    val createdAt: Instant = Instant.now(),
    val commentVotes: List<CommentVote> = emptyList()
)

data class CommentVote(
    val commentId: Int,
    val voteId: Int
)

