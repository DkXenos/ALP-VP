package com.jason.alp_vp.data.dto.comment

data class CommentRequest(
    val filters: Filters,
    val includeVotes: Boolean,
    val limit: Int,
    val page: Int,
    val q: String,
    val sort: String
)