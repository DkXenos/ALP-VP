package com.jason.alp_vp.data.repository

import com.jason.alp_vp.data.service.BountyService
import com.jason.alp_vp.data.service.BountiesResponse
import com.jason.alp_vp.data.service.BountyDetailResponse
import com.jason.alp_vp.data.service.ClaimBountyResponse
import com.jason.alp_vp.data.service.UnclaimBountyResponse
import com.jason.alp_vp.data.service.MyBountiesResponse
import com.jason.alp_vp.data.service.SubmitWorkRequest
import com.jason.alp_vp.data.service.SubmitWorkResponse
import com.jason.alp_vp.data.service.ApplicantsResponse
import com.jason.alp_vp.data.service.SelectWinnerResponse

/**
 * BountyRepository - Handles all bounty-related business logic
 *
 * Responsibilities:
 * - Fetch bounty listings and details
 * - Claim/Unclaim bounties (users)
 * - Submit work for bounties (users)
 * - Manage applicants and select winners (companies)
 * - Error handling and data validation
 *
 * Flow: ViewModel → BountyRepository → BountyService → Backend API
 */
class BountyRepository(private val service: BountyService) {

    /**
     * Get all available bounties
     *
     * @return BountiesResponse with list of all bounties
     * @throws Exception if the API call fails
     */
    suspend fun getAllBounties(): BountiesResponse {
        val response = service.getAllBounties()

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: "Unknown error"
            throw Exception("Failed to fetch bounties: ${response.code()} - $errorMessage")
        }

        return response.body()
            ?: throw Exception("Bounties response body is null")
    }

    /**
     * Get detailed information about a specific bounty
     *
     * @param bountyId The UUID of the bounty
     * @return BountyDetailResponse with detailed bounty information
     * @throws Exception if the API call fails
     */
    suspend fun getBountyById(bountyId: String): BountyDetailResponse {
        if (bountyId.isBlank()) {
            throw IllegalArgumentException("Bounty ID cannot be empty")
        }

        val response = service.getBountyById(bountyId)

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: "Unknown error"
            throw Exception("Failed to fetch bounty details: ${response.code()} - $errorMessage")
        }

        return response.body()
            ?: throw Exception("Bounty detail response body is null")
    }

    /**
     * Claim a bounty (assigns it to the current user)
     *
     * @param bountyId The UUID of the bounty to claim
     * @return ClaimBountyResponse with updated bounty data
     * @throws Exception if the API call fails or bounty is already claimed
     */
    suspend fun claimBounty(bountyId: String): ClaimBountyResponse {
        if (bountyId.isBlank()) {
            throw IllegalArgumentException("Bounty ID cannot be empty")
        }

        val response = service.claimBounty(bountyId)

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: "Unknown error"

            // Provide user-friendly error messages
            val friendlyMessage = when (response.code()) {
                400 -> "This bounty is already claimed"
                404 -> "Bounty not found"
                403 -> "You don't have permission to claim this bounty"
                else -> "Failed to claim bounty: $errorMessage"
            }

            throw Exception(friendlyMessage)
        }

        return response.body()
            ?: throw Exception("Claim bounty response body is null")
    }

    /**
     * Unclaim a bounty (removes assignment from current user)
     *
     * @param bountyId The UUID of the bounty to unclaim
     * @return UnclaimBountyResponse with success message
     * @throws Exception if the API call fails
     */
    suspend fun unclaimBounty(bountyId: String): UnclaimBountyResponse {
        if (bountyId.isBlank()) {
            throw IllegalArgumentException("Bounty ID cannot be empty")
        }

        val response = service.unclaimBounty(bountyId)

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: "Unknown error"

            val friendlyMessage = when (response.code()) {
                400 -> "You haven't claimed this bounty"
                404 -> "Bounty not found"
                403 -> "You don't have permission to unclaim this bounty"
                else -> "Failed to unclaim bounty: $errorMessage"
            }

            throw Exception(friendlyMessage)
        }

        return response.body()
            ?: throw Exception("Unclaim bounty response body is null")
    }

    /**
     * Get all bounties claimed by the current user
     *
     * @return MyBountiesResponse with list of user's claimed bounties
     * @throws Exception if the API call fails
     */
    suspend fun getMyClaimedBounties(): MyBountiesResponse {
        val response = service.getMyClaimedBounties()

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: "Unknown error"
            throw Exception("Failed to fetch claimed bounties: ${response.code()} - $errorMessage")
        }

        return response.body()
            ?: throw Exception("My bounties response body is null")
    }

    /**
     * Submit work for a claimed bounty
     *
     * @param bountyId The UUID of the bounty
     * @param submissionUrl URL to the submitted work (e.g., GitHub, Google Drive)
     * @param submissionNotes Optional notes about the submission
     * @return SubmitWorkResponse with updated bounty data
     * @throws Exception if the API call fails or validation fails
     */
    suspend fun submitWork(
        bountyId: String,
        submissionUrl: String,
        submissionNotes: String? = null
    ): SubmitWorkResponse {
        // Validation
        if (bountyId.isBlank()) {
            throw IllegalArgumentException("Bounty ID cannot be empty")
        }

        if (submissionUrl.isBlank()) {
            throw IllegalArgumentException("Submission URL cannot be empty")
        }

        // Basic URL validation
        if (!submissionUrl.startsWith("http://") && !submissionUrl.startsWith("https://")) {
            throw IllegalArgumentException("Submission URL must be a valid HTTP/HTTPS URL")
        }

        val request = SubmitWorkRequest(
            submissionUrl = submissionUrl.trim(),
            submissionNotes = submissionNotes?.trim()
        )

        val response = service.submitWork(bountyId, request)

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: "Unknown error"

            val friendlyMessage = when (response.code()) {
                400 -> "Invalid submission or bounty not claimed by you"
                404 -> "Bounty not found"
                403 -> "You don't have permission to submit work for this bounty"
                else -> "Failed to submit work: $errorMessage"
            }

            throw Exception(friendlyMessage)
        }

        return response.body()
            ?: throw Exception("Submit work response body is null")
    }

    /**
     * Get all applicants who claimed a bounty (Company feature)
     *
     * @param bountyId The UUID of the bounty
     * @return ApplicantsResponse with list of applicants and their submissions
     * @throws Exception if the API call fails or user is not authorized
     */
    suspend fun getBountyApplicants(bountyId: String): ApplicantsResponse {
        if (bountyId.isBlank()) {
            throw IllegalArgumentException("Bounty ID cannot be empty")
        }

        val response = service.getBountyApplicants(bountyId)

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: "Unknown error"

            val friendlyMessage = when (response.code()) {
                403 -> "Only companies can view applicants"
                404 -> "Bounty not found"
                else -> "Failed to fetch applicants: $errorMessage"
            }

            throw Exception(friendlyMessage)
        }

        return response.body()
            ?: throw Exception("Applicants response body is null")
    }

    /**
     * Select a winner for a bounty (Company feature)
     * Awards XP and money to the winner
     *
     * @param bountyId The UUID of the bounty
     * @param userId The ID of the winning user
     * @return SelectWinnerResponse with success confirmation
     * @throws Exception if the API call fails or user is not authorized
     */
    suspend fun selectWinner(bountyId: String, userId: Int): SelectWinnerResponse {
        if (bountyId.isBlank()) {
            throw IllegalArgumentException("Bounty ID cannot be empty")
        }

        if (userId <= 0) {
            throw IllegalArgumentException("Invalid user ID")
        }

        val response = service.selectWinner(bountyId, userId)

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: "Unknown error"

            val friendlyMessage = when (response.code()) {
                400 -> "Invalid winner selection or bounty already completed"
                403 -> "Only companies can select winners"
                404 -> "Bounty or user not found"
                else -> "Failed to select winner: $errorMessage"
            }

            throw Exception(friendlyMessage)
        }

        return response.body()
            ?: throw Exception("Select winner response body is null")
    }
}

