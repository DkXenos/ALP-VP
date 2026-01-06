package com.jason.alp_vp.data.service

import com.jason.alp_vp.data.dto.vote.CreateVoteRequest
import com.jason.alp_vp.data.dto.vote.CreateVoteResponse
import com.jason.alp_vp.data.dto.comment.LinkCommentVoteRequest
import com.jason.alp_vp.data.dto.comment.LinkCommentVoteResponse
import retrofit2.Response
import retrofit2.http.*

interface VoteService {

    @POST("votes")
    suspend fun createVote(
        @Body request: CreateVoteRequest
    ): Response<CreateVoteResponse>

    @POST("comment-votes")
    suspend fun linkCommentVote(
        @Body request: LinkCommentVoteRequest
    ): Response<LinkCommentVoteResponse>

    @DELETE("votes/{id}")
    suspend fun deleteVote(
        @Path("id") voteId: Int
    ): Response<LinkCommentVoteResponse>
}

