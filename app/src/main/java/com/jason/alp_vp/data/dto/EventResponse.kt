package com.jason.alp_vp.data.dto

data class EventResponse(
    val company_id: Int,
    val company_name: String,
    val created_at: String,
    val current_registrations: Int,
    val description: String,
    val event_date: String,
    val id: Int,
    val registered_quota: Int,
    val registered_users: List<RegisteredUser>,
    val title: String
)