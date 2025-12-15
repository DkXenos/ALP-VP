package com.jason.alp_vp.data.dto.event

data class EventRequest(
    val filters: Filters,
    val limit: Int,
    val page: Int,
    val q: String,
    val sort: String
)