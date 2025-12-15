package com.jason.alp_vp.data.service

import com.jason.alp_vp.data.dto.post.PostResponse
import retrofit2.Response
import retrofit2.http.*

interface PostService {

    @POST("/posts")
    suspend fun createPost(
        @Body post: CreatePostRequest
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
        @Body post: UpdatePostRequest
    ): Response<PostResponse>

    @DELETE("/posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Int
    ): Response<Unit>
}

data class CreatePostRequest(
    val userId: Int,
    val content: String,
    val image: String? = null
)

data class UpdatePostRequest(
    val content: String? = null,
    val image: String? = null
)

