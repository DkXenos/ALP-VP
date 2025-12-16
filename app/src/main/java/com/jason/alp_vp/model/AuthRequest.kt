package com.jason.alp_vp.model

data class AuthRequest(
    val username: String? = null,
    val email: String,
    val password: String,
    val role: String? = null
)

// For Registration - all fields required
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: String = "TALENT"
)

// For Login - only email and password
data class LoginRequest(
    val email: String,
    val password: String
)

