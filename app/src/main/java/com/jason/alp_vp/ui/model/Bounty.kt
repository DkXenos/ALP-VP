package com.jason.alp_vp.ui.model

data class Bounty(
    val id: String,
    val title: String,
    val company: String,
    val deadline: String,
    val rewardXp: Int,
    val rewardMoney: Int,
    val status: String,
    val claimedBy: String? = null,  // User ID who claimed this bounty
    val assignedAt: String? = null,  // When it was claimed
    val isCompleted: Boolean = false
)
