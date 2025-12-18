package com.jason.alp_vp.ui.model

data class User(
    val id: String,
    val username: String,
    val email: String,
    val password: String = "",
    val bio: String = "",
    val avatar: String? = null,
    val level: Int = 1,
    val xp: Int = 0,
    val rating: Double = 0.0
)

