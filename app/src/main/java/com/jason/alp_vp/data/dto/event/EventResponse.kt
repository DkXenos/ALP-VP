package com.jason.alp_vp.data.dto.event

import com.google.gson.annotations.SerializedName

// Wrapper for API responses - backend wraps data in { "data": ... }
data class EventResponse(
    @SerializedName("data") val data: List<EventResponseItem>
)

