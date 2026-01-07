package com.jason.alp_vp.model

// User login response
data class AuthResponse(
    val data: UserData
)

data class UserData(
    val id: Int,
    val username: String?,
    val email: String,
    val role: String? = null,  // Make nullable with default since backend doesn't always send it
    val token: String
)

// Company login response (with data wrapper)
data class CompanyAuthResponse(
    val data: CompanyAuthData  // Backend wraps everything in "data"
)

data class CompanyAuthData(
    val token: String?,
    val company: CompanyData?
)

data class CompanyData(
    val id: Int,
    val name: String,  // Companies have "name" instead of "username"
    val email: String
)

