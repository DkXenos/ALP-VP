package com.jason.alp_vp.data.dto.comment

data class LinkCommentVoteRequest(
    val comment_id: Int,
    val vote_id: Int
)

data class LinkCommentVoteResponse(
    val success: Boolean,
    val message: String
)
