package com.jason.alp_vp.data.dto

data class UserResponse(
    val email: String,
    val id: Int,
    val token: String,
    val username: String
)