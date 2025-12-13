package com.jason.alp_vp.ui.model

import java.time.Instant

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val eventDate: Instant,
    val companyId: Int,
    val registeredQuota: Int,
    val createdAt: Instant = Instant.now(),
    val registrations: List<EventRegistration> = emptyList()
)

data class EventRegistration(
    val userId: Int,
    val eventId: Int
)
