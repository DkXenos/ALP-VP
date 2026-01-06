package com.jason.alp_vp.data.service

import com.jason.alp_vp.data.dto.comment.CommentRequest
import com.jason.alp_vp.data.dto.comment.CommentResponse
import retrofit2.Response
import retrofit2.http.*

interface CommentService {

    @POST("/comments")
    suspend fun createComment(
        @Body request: CommentRequest,
        @Header("Authorization") token: String
    ): Response<CommentResponse>

    @GET("/posts/{postId}/comments")
    suspend fun getCommentsByPost(
        @Path("postId") postId: Int
    ): Response<List<CommentResponse>>

    @DELETE("/comments/{id}")
    suspend fun deleteComment(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>
}

data class CommentResponse(
    val id: Int,
    val post_id: Int,
    val content: String,
    val created_at: String
)

