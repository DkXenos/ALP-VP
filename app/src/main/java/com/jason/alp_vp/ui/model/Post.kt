package com.jason.alp_vp.ui.model

/**
 * Minimal models for Post and Vote. Keep calculations (counts) in ViewModel as requested.
 */

data class Post(
    val id: String,
    val content: String,
    val createdAt: Long,
    val votes: List<Vote> = emptyList()
)
