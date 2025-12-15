package com.jason.alp_vp.ui.model

import java.time.Instant

data class Bounty(
    val id: String,
    val title: String,
    val description: String,
    val reward: Double,
    val difficulty: String, // "Easy", "Medium", "Hard"
    val minLevelRequired: Int,
    val type: String, // "bounty" or "event"
    val status: String, // "active", "expired", "completed"
    val createdAt: Instant,
    val deadline: Instant? = null,
    val quota: Int? = null, // for events
    val countdownEnd: Instant? = null // for events
)
