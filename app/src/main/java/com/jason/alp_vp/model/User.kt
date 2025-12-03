package com.jason.alp_vp.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val role: UserRole,
    val level: Int,
    val xp: Int,
    val walletBalance: Double,
    val avatarUrl: String = "",
    val skills: Map<String, Int> = emptyMap() // For radar chart
)

enum class UserRole {
    TALENT,
    COMPANY
}

