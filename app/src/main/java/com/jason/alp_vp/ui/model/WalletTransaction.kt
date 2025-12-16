package com.jason.alp_vp.ui.model

import java.time.Instant

data class WalletTransaction(
    val id: String,
    val amount: Double,
    val type: String, // "earned", "withdrawn"
    val description: String,
    val status: String, // "completed", "pending", "failed"
    val createdAt: Instant
)

