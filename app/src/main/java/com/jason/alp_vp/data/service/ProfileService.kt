package com.jason.alp_vp.data.service

import retrofit2.Response
import retrofit2.http.*

interface ProfileService {

    // Get user profile with posts, events, and bounties
    @GET("profile")
    suspend fun getProfile(): Response<ProfileResponse>

    // Get user stats
    @GET("profile/stats")
    suspend fun getProfileStats(): Response<ProfileStatsResponse>

    // Update profile
    @PUT("profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UpdateProfileResponse>

    // Get user's posts
    @GET("profile/posts")
    suspend fun getUserPosts(): Response<UserPostsResponse>

    // Get user's events
    @GET("profile/events")
    suspend fun getUserEvents(): Response<UserEventsResponse>
}

// Request/Response models
data class UpdateProfileRequest(
    val username: String,
    val email: String
)

data class ProfileResponse(
    val data: ProfileData
)

data class ProfileData(
    val id: Int,
    val username: String,
    val email: String,
    val role: String,
    val posts: List<PostItem>,
    val events: List<EventItem>,
    val bounties: List<ProfileBountyItem>
)

data class PostItem(
    val id: Int,
    val user_id: Int,
    val content: String,
    val image: String?,
    val created_at: String,
    val username: String? = null,
    val comments: List<CommentItem>
)

data class CommentItem(
    val id: Int,
    val post_id: Int,
    val content: String,
    val created_at: String
)

data class EventItem(
    val id: Int,
    val title: String,
    val description: String,
    val event_date: String,
    val company_id: Int,
    val company_name: String,
    val registered_quota: Int,
    val current_registrations: Int,
    val created_at: String
)

data class ProfileBountyItem(
    val id: String,
    val title: String,
    val company: CompanyInfo?,  // Changed from String to CompanyInfo object
    val deadline: String,
    val rewardXp: Int,
    val rewardMoney: Int,
    val status: String,
    val assignedAt: String?,
    val isCompleted: Boolean,
    val completedAt: String?
)

data class CompanyInfo(
    val id: Int,
    val name: String,
    val email: String? = null,
    val description: String? = null
)

data class ProfileStatsResponse(
    val data: ProfileStats
)

data class ProfileStats(
    val totalPosts: Int,
    val totalEvents: Int,
    val totalBounties: Int,
    val activeBounties: Int,
    val completedBounties: Int,
    val totalXpEarned: Int,
    val totalMoneyEarned: Int
)

data class UpdateProfileResponse(
    val success: Boolean,
    val message: String,
    val data: ProfileData
)

data class UserPostsResponse(
    val data: List<PostItem>
)

data class UserEventsResponse(
    val data: List<EventItem>
)

