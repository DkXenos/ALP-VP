package com.jason.alp_vp.ui.model

data class Company(
    val id: String,
    val name: String,
    val description: String,
    val logo: String? = null,
    val walletBalance: Double = 0.0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val followedPagesCount: Int = 0
)
