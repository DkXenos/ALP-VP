package com.jason.alp_vp.data.service

import com.jason.alp_vp.data.dto.comment.CommentResponse
import retrofit2.Response
import retrofit2.http.*

interface CommentService {

    @POST("/comments")
    suspend fun createComment(
        @Body comment: CreateCommentRequest
    ): Response<CommentResponse>

    @GET("/comments/{id}")
    suspend fun getCommentById(
        @Path("id") id: Int
    ): Response<CommentResponse>

    @PUT("/comments/{id}")
    suspend fun updateComment(
        @Path("id") id: Int,
        @Body comment: UpdateCommentRequest
    ): Response<CommentResponse>

    @DELETE("/comments/{id}")
    suspend fun deleteComment(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("/posts/{postId}/comments")
    suspend fun getCommentsByPostId(
        @Path("postId") postId: Int
    ): Response<List<CommentResponse>>
}

data class CreateCommentRequest(
    val postId: Int,
    val userId: Int,
    val content: String
)

data class UpdateCommentRequest(
    val content: String
)

