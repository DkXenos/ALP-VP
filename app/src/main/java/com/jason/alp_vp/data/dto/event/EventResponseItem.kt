package com.jason.alp_vp.data.dto.event

import com.google.gson.annotations.SerializedName

// Flat structure - backend returns company_name directly in event object
data class EventResponseItem(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("event_date") val event_date: String,
    @SerializedName("company_id") val company_id: Int,
    @SerializedName("company_name") val company_name: String,  // Flat - NOT nested in company object
    @SerializedName("registered_quota") val registered_quota: Int,
    @SerializedName("current_registrations") val current_registrations: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("registered_users") val registered_users: List<RegisteredUserItem>?
)

data class RegisteredUserItem(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String
)

