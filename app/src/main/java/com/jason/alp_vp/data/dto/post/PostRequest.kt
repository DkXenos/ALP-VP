package com.jason.alp_vp.data.dto.post

data class PostRequest(
    val filters: Filters,
    val limit: Int,
    val page: Int,
    val q: String,
    val sort: String
)