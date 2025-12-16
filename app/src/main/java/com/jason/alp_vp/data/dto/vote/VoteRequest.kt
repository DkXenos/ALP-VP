package com.jason.alp_vp.data.dto.vote

data class VoteRequest(
    val filters: Filters,
    val limit: Int,
    val page: Int,
    val sort: String
)