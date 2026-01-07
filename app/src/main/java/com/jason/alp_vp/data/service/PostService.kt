package com.jason.alp_vp.data.service

import com.jason.alp_vp.data.dto.post.PostRequest
import com.jason.alp_vp.data.dto.post.PostResponseItem
import retrofit2.Response
import retrofit2.http.*

interface PostService {

    @POST("/posts")
    suspend fun createPost(
        @Body request: PostRequest,
        @Header("Authorization") token: String
    ): Response<ApiResponseWrapper<PostResponseItem>>

    @GET("/posts")
    suspend fun getAllPosts(): Response<ApiResponseWrapper<List<PostResponseItem>>>

    @GET("/posts/{id}")
    suspend fun getPostById(
        @Path("id") id: Int
    ): Response<ApiResponseWrapper<PostResponseItem>>

    @PUT("/posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body request: UpdatePostRequest,
        @Header("Authorization") token: String
    ): Response<ApiResponseWrapper<PostResponseItem>>

    @DELETE("/posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Unit>
}

// API Response Wrapper - Backend wraps responses in { "data": ... }
data class ApiResponseWrapper<T>(
    val data: T
)

data class UpdatePostRequest(
    val content: String? = null,
    val image: String? = null
)
