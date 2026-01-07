package com.jason.alp_vp.data.dto.event

import com.google.gson.annotations.SerializedName

data class EventRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("event_date") val event_date: String,
    @SerializedName("company_id") val company_id: String,
    @SerializedName("registered_quota") val registered_quota: Int
)

