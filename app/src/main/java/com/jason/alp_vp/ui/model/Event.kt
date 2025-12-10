package com.jason.alp_vp.ui.model

import java.time.Duration
import java.time.Instant

data class EventPost(
    val id: String,
    val title: String,
    val organizer: String,
    val description: String,
    val registered: Int,
    val capacity: Int,
    val badgeEmoji: String = "📅",
    val isEvent: Boolean = true,
    val createdAt: Instant = Instant.now(),
    val timeRemaining: Duration? = null // optional: compute remaining time if needed
)
