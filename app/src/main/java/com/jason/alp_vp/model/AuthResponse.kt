package com.jason.alp_vp.model
import com.google.gson.annotations.SerializedName
/**
 * Authentication response from backend
 */
data class AuthResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("user")
    val user: User? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("error")
    val error: String? = null
)
/**
 * User data
 */
data class User(
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("createdAt")
    val createdAt: String? = null
)
