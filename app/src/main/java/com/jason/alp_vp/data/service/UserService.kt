package com.jason.alp_vp.data.service

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @POST("/users")
    suspend fun register(
        @Body user: UserRegisterRequest
    ): Response<UserAuthResponse>

    @POST("/auth/login")
    suspend fun login(
        @Body credentials: UserLoginRequest
    ): Response<UserAuthResponse>

    @GET("/users")
    suspend fun getAllUsers(): Response<List<UserResponse>>

    @GET("/users/{id}")
    suspend fun getUserById(
        @Path("id") id: Int
    ): Response<UserResponse>

    @PUT("/users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body user: UpdateUserRequest
    ): Response<UserResponse>

    @DELETE("/users/{id}")
    suspend fun deleteUser(
        @Path("id") id: Int
    ): Response<Unit>

    // ========== PROFILE IMAGE ENDPOINTS ==========

    @Multipart
    @POST("/users/me/profile-image")
    suspend fun uploadProfileImage(
        @Part profile_image: MultipartBody.Part
    ): Response<ProfileImageResponse>

    @DELETE("/users/me/profile-image")
    suspend fun deleteProfileImage(): Response<ProfileImageResponse>
}

data class UserRegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val profile_image: String? = null
)

data class UserLoginRequest(
    val email: String,
    val password: String
)

data class UserAuthResponse(
    val token: String,
    val user: UserResponse
)

data class UserResponse(
    val id: Int,
    val username: String,
    val email: String,
    val xp: Int? = 0,
    val balance: Int? = 0,
    val profile_image: String?
)

data class UpdateUserRequest(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val profile_image: String? = null
)


data class ProfileImageResponse(
    val data: ProfileImageData
)

data class ProfileImageData(
    val profile_image: String?
)