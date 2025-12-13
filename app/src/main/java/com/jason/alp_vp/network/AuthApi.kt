package com.jason.alp_vp.network

import com.jason.alp_vp.model.AuthRequest
import com.jason.alp_vp.model.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("register")
    suspend fun register(@Body request: AuthRequest): AuthResponse

    @POST("login")
    suspend fun login(@Body request: AuthRequest): AuthResponse
}
