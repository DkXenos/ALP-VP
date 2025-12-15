package com.jason.alp_vp.ui.model

data class PaymentMethod(
    val id: String,
    val type: String, // "bank", "ewallet", "credit_card"
    val name: String,
    val accountNumber: String,
    val isDefault: Boolean = false
)

