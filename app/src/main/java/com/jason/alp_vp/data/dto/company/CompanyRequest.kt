package com.jason.alp_vp.data.dto.company

data class CompanyRequest(
    val filters: Filters,
    val limit: Int,
    val page: Int,
    val q: String,
    val sort: String
)