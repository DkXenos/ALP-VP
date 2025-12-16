package com.jason.alp_vp.ui.model

data class CommentVote(
    val commentId: Int,
    val voteId: Int,
    val voteType: String = "upvote" // "upvote" or "downvote"
)

