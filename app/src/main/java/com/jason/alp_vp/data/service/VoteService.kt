package com.jason.alp_vp.data.service

import retrofit2.Response
import retrofit2.http.*

interface VoteService {

    @POST("/votes")
    suspend fun addVote(
        @Body vote: AddVoteRequest
    ): Response<VoteResponse>

    @DELETE("/votes/{voteId}")
    suspend fun removeVote(
        @Path("voteId") voteId: Int
    ): Response<Unit>
}

data class AddVoteRequest(
    val commentId: Int,
    val userId: Int,
    val voteType: String // "upvote" or "downvote"
)

data class VoteResponse(
    val id: Int,
    val commentId: Int,
    val userId: Int,
    val voteType: String,
    val createdAt: String
)

