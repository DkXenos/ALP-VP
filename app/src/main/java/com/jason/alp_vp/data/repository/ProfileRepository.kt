package com.jason.alp_vp.data.repository

import com.jason.alp_vp.data.service.ProfileService
import com.jason.alp_vp.data.service.ProfileResponse
import com.jason.alp_vp.data.service.ProfileStatsResponse
import com.jason.alp_vp.data.service.UpdateProfileRequest
import com.jason.alp_vp.data.service.UpdateProfileResponse
import com.jason.alp_vp.data.service.UserPostsResponse
import com.jason.alp_vp.data.service.UserEventsResponse

/**
 * ProfileRepository - Handles all profile-related business logic
 *
 * Responsibilities:
 * - Fetch user profile data (posts, events, bounties)
 * - Update user profile information
 * - Get user statistics (XP, earnings, etc.)
 * - Error handling and data transformation
 *
 * Flow: ViewModel → ProfileRepository → ProfileService → Backend API
 */
class ProfileRepository(private val service: ProfileService) {

    /**
     * Get the current user's profile (authenticated via JWT token)
     *
     * @return ProfileResponse with user data including posts, events, and bounties
     * @throws Exception if the API call fails
     */
    suspend fun getProfile(): ProfileResponse {
        val response = service.getProfile()

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: "Unknown error"
            throw Exception("Failed to fetch profile: ${response.code()} - $errorMessage")
        }

        return response.body()
            ?: throw Exception("Profile response body is null")
    }

    /**
     * Get user statistics (total posts, events, bounties, XP, earnings)
     *
     * @return ProfileStatsResponse with aggregated statistics
     * @throws Exception if the API call fails
     */
    suspend fun getProfileStats(): ProfileStatsResponse {
        val response = service.getProfileStats()

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: "Unknown error"
            throw Exception("Failed to fetch profile stats: ${response.code()} - $errorMessage")
        }

        return response.body()
            ?: throw Exception("Profile stats response body is null")
    }

    /**
     * Update user profile (username and email)
     *
     * @param username New username
     * @param email New email address
     * @return UpdateProfileResponse with updated profile data
     * @throws Exception if the API call fails or validation fails
     */
    suspend fun updateProfile(username: String, email: String): UpdateProfileResponse {
        // Basic validation
        if (username.isBlank()) {
            throw IllegalArgumentException("Username cannot be empty")
        }

        if (email.isBlank() || !email.contains("@")) {
            throw IllegalArgumentException("Invalid email address")
        }

        val request = UpdateProfileRequest(
            username = username.trim(),
            email = email.trim()
        )

        val response = service.updateProfile(request)

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: "Unknown error"
            throw Exception("Failed to update profile: ${response.code()} - $errorMessage")
        }

        return response.body()
            ?: throw Exception("Update profile response body is null")
    }

    /**
     * Get all posts created by the user
     *
     * @return UserPostsResponse with list of posts
     * @throws Exception if the API call fails
     */
    suspend fun getUserPosts(): UserPostsResponse {
        val response = service.getUserPosts()

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: "Unknown error"
            throw Exception("Failed to fetch user posts: ${response.code()} - $errorMessage")
        }

        return response.body()
            ?: throw Exception("User posts response body is null")
    }

    /**
     * Get all events created by the company/user
     *
     * @return UserEventsResponse with list of events
     * @throws Exception if the API call fails
     */
    suspend fun getUserEvents(): UserEventsResponse {
        val response = service.getUserEvents()

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: "Unknown error"
            throw Exception("Failed to fetch user events: ${response.code()} - $errorMessage")
        }

        return response.body()
            ?: throw Exception("User events response body is null")
    }
}

