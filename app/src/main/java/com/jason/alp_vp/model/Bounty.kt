package com.jason.alp_vp.model

data class Bounty(
    val id: String,
    val title: String,
    val companyName: String,
    val description: String,
    val price: Double,
    val xp: Int,
    val minLevel: Int,
    val status: BountyStatus,
    val isEvent: Boolean,
    val requirements: List<String> = emptyList(),
    val deadline: String = "",
    val category: String = "General"
)

enum class BountyStatus {
    OPEN,
    IN_PROGRESS,
    COMPLETED,
    CLOSED
}

