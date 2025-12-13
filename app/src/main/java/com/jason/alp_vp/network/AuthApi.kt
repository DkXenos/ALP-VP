package com.jason.alp_vp.network

import com.jason.alp_vp.model.AuthResponse
import com.jason.alp_vp.model.LoginRequest
import com.jason.alp_vp.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Authentication API endpoints
 * Backend should implement these routes
 */
interface AuthApi {

    /**
     * Login user
     * POST /api/login
     */
    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    /**
     * Register new user
     * POST /api/register
     */
    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    /**
     * Verify token and get user info
     * GET /api/me
     */
    @GET("me")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): Response<AuthResponse>

    /**
     * Logout (optional - mainly for token invalidation on backend)
     * POST /api/logout
     */
    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<AuthResponse>
}

