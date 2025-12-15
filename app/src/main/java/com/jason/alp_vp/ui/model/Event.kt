package com.jason.alp_vp.ui.model

import java.time.Instant

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val eventDate: Instant,
    val companyId: Int,
    val companyName: String = "",
    val registeredQuota: Int,
    val currentRegistrations: Int = 0,
    val createdAt: Instant = Instant.now()
)
