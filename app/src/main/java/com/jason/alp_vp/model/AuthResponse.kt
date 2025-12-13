package com.jason.alp_vp.model

data class AuthResponse(
    val data: UserData
)

data class UserData(
    val id: Int,
    val username: String,
    val email: String,
    val role: String,
    val token: String
)
