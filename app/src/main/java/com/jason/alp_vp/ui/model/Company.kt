package com.jason.alp_vp.ui.model

import com.jason.alp_vp.data.dto.company.CompanyResponseItem

data class Company(
    val id: Int,
    val name: String,
    val email: String,
    val description: String,
    val logo: String? = null,
    val createdAt: String,
    val walletBalance: Double = 0.0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val followedPagesCount: Int = 0
)

// Extension function to convert DTO to Model
@Suppress("unused")
fun CompanyResponseItem.toModel() = Company(
    id = this.id,
    name = this.name,
    email = this.email,
    description = this.description,
    logo = this.logo,
    createdAt = this.createdAt
)
