package com.jason.alp_vp.data.service

import com.jason.alp_vp.data.dto.comment.CommentResponse
import com.jason.alp_vp.data.dto.post.PostRequest
import com.jason.alp_vp.data.dto.post.PostResponse
import retrofit2.Response
import retrofit2.http.*

interface PostService {

    @POST("/posts")
    suspend fun createPost(
        @Body request: PostRequest,
        @Header("Authorization") token: String
    ): Response<PostResponse>

    @GET("/posts")
    suspend fun getAllPosts(): Response<List<PostResponse>>

    @GET("/posts/{id}")
    suspend fun getPostById(
        @Path("id") id: Int
    ): Response<PostResponse>

    @PUT("/posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body request: UpdatePostRequest,
        @Header("Authorization") token: String
    ): Response<PostResponse>

    @DELETE("/posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>
}

data class PostResponse(
    val id: Int,
    val user_id: Int,
    val content: String,
    val image: String?,
    val created_at: String,
    val comments: List<CommentResponse>? = null
)

data class UpdatePostRequest(
    val content: String? = null,
    val image: String? = null
)
