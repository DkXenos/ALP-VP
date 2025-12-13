package com.jason.alp_vp.data.api.model
import com.google.gson.annotations.SerializedName
/**
 * API Response wrapper for all endpoints
 */
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("error")
    val error: String? = null
)
/**
 * Bounty response from API
 */
data class BountyResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("company")
    val company: String,
    @SerializedName("deadline")
    val deadline: String,
    @SerializedName("rewardXp")
    val rewardXp: Int,
    @SerializedName("rewardMoney")
    val rewardMoney: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("requirements")
    val requirements: List<String>? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null
)
/**
 * Application submission request
 */
data class ApplicationRequest(
    @SerializedName("bountyId")
    val bountyId: String,
    @SerializedName("portfolioLinks")
    val portfolioLinks: List<String>,
    @SerializedName("cvImageUrl")
    val cvImageUrl: String,
    @SerializedName("whyHireYou")
    val whyHireYou: String
)
/**
 * Application response
 */
data class ApplicationResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("bountyId")
    val bountyId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("submittedAt")
    val submittedAt: String
)
