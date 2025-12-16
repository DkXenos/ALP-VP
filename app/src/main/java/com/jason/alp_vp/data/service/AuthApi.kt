package com.jason.alp_vp.data.service

import com.jason.alp_vp.model.AuthRequest
import com.jason.alp_vp.model.AuthResponse
import com.jason.alp_vp.model.CompanyAuthResponse
import com.jason.alp_vp.model.LoginRequest
import com.jason.alp_vp.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("companies/login")
    suspend fun companyLogin(@Body request: LoginRequest): CompanyAuthResponse
}

