package com.jason.alp_vp.data.dto.event

data class EventResponseItem(
    val id: Int,
    val title: String,
    val description: String,
    val event_date: String,
    val company_id: Int,
    val company_name: String,  // Flattened - NOT nested in company object
    val registered_quota: Int,
    val current_registrations: Int,
    val created_at: String,
    val registered_users: List<RegisteredUser>? = null
)

data class RegisteredUser(
    val id: Int,
    val username: String,
    val email: String
)