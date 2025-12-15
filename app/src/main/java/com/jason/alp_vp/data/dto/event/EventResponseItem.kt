package com.jason.alp_vp.data.dto.event

data class EventResponseItem(
    val company: Company,
    val companyId: Int,
    val createdAt: String,
    val currentRegistrations: Int,
    val description: String,
    val eventDate: String,
    val id: Int,
    val registeredQuota: Int,
    val title: String
)