package com.jason.alp_vp.data.dto.vote

data class CreateVoteRequest(
    val user_id: Int,
    val vote_type: String // "upvote" or "downvote"
)

data class CreateVoteResponse(
    val success: Boolean,
    val message: String,
    val data: VoteData?
)

data class VoteData(
    val id: Int,
    val user_id: Int,
    val vote_type: String,
    val created_at: String
)
