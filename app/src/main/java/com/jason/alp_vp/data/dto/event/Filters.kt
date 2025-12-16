package com.jason.alp_vp.data.dto.event

data class Filters(
    val companyId: Int,
    val dateFrom: String,
    val dateTo: String,
    val upcomingOnly: Boolean
)