package com.jason.alp_vp.data.service

import com.jason.alp_vp.data.dto.comment.CommentRequest
import com.jason.alp_vp.data.dto.vote.VoteRequest
import retrofit2.Response
import retrofit2.http.*

interface VoteService {

    @POST("/votes")
    suspend fun createVote(
        @Body request: VoteRequest,
        @Header("Authorization") token: String
    ): Response<VoteResponse>

    @POST("/comment-votes")
    suspend fun linkCommentVote(
        @Body request: CommentRequest,
        @Header("Authorization") token: String
    ): Response<Unit>
}

data class VoteResponse(
    val id: Int,
    val vote_type: String
)

