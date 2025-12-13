package com.jason.alp_vp.model

data class AuthRequest(
    val username: String? = null,
    val email: String,
    val password: String,
    val role: String? = null
)
