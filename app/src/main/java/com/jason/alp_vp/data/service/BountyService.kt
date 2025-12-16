package com.jason.alp_vp.data.service

import com.jason.alp_vp.ui.model.Bounty
import retrofit2.Response
import retrofit2.http.*

interface BountyService {

    // 1. Get All Bounties
    @GET("bounties")
    suspend fun getAllBounties(): Response<BountiesResponse>

    // 2. Get Bounty by ID
    @GET("bounties/{id}")
    suspend fun getBountyById(
        @Path("id") id: String
    ): Response<BountyDetailResponse>

    // 3. Claim Bounty
    @POST("bounties/{id}/claim")
    suspend fun claimBounty(
        @Path("id") id: String
    ): Response<ClaimBountyResponse>

    // 4. Unclaim Bounty
    @DELETE("bounties/{id}/unclaim")
    suspend fun unclaimBounty(
        @Path("id") id: String
    ): Response<UnclaimBountyResponse>

    // 5. Get My Claimed Bounties
    @GET("my-bounties")
    suspend fun getMyClaimedBounties(): Response<MyBountiesResponse>

    // 6. Submit Work (Users only)
    @POST("bounties/{id}/submit")
    suspend fun submitWork(
        @Path("id") id: String,
        @Body request: SubmitWorkRequest
    ): Response<SubmitWorkResponse>

    // 7. Get Bounty Applicants (Companies only)
    @GET("bounties/{id}/applicants")
    suspend fun getBountyApplicants(
        @Path("id") id: String
    ): Response<ApplicantsResponse>

    // 8. Select Winner (Companies only)
    @POST("bounties/{id}/select-winner/{userId}")
    suspend fun selectWinner(
        @Path("id") bountyId: String,
        @Path("userId") userId: Int
    ): Response<SelectWinnerResponse>
}

// Response data classes
data class BountiesResponse(
    val success: Boolean,
    val data: List<BountyItem>
)

data class BountyItem(
    val id: String,  // ✅ Changed from Int to String (UUID)
    val title: String,
    val description: String?,
    val company: String?,  // Added company field
    val rewardXp: Int?,  // Changed from reward to rewardXp
    val rewardMoney: Int?,  // Added rewardMoney
    val status: String,
    val deadline: String?,
    val companyId: String?,  // Changed from Int to String
    val companyName: String?,
    val createdAt: String?,
    val claimedBy: String?,  // Changed from Int to String
    val assignedAt: String?,  // Added for AssignedBounty
    val isCompleted: Boolean?,  // Added for AssignedBounty
    val completedAt: String?,  // Added for AssignedBounty
    val submissionUrl: String? = null,  // Submission URL
    val submissionNotes: String? = null,  // Optional notes
    val submittedAt: String? = null  // When work was submitted
)

data class BountyDetailResponse(
    val success: Boolean,
    val data: BountyItem
)

data class ClaimBountyResponse(
    val success: Boolean,
    val message: String,
    val data: BountyItem
)

data class UnclaimBountyResponse(
    val success: Boolean,
    val message: String
)

data class MyBountiesResponse(
    val success: Boolean,
    val data: List<BountyItem>
)

// Extension function to convert API response to UI model
fun BountyItem.toUiModel(): Bounty {
    return Bounty(
        id = this.id,  // ✅ No need to convert - already String
        title = this.title,
        company = this.company ?: this.companyName ?: "Unknown Company",
        deadline = this.deadline ?: "",
        rewardXp = this.rewardXp ?: 0,
        rewardMoney = this.rewardMoney ?: 0,
        status = this.status,
        claimedBy = this.claimedBy,
        assignedAt = this.assignedAt,
        isCompleted = this.isCompleted ?: false
    )
}

// New Request/Response models for submission workflow

// Submit work request
data class SubmitWorkRequest(
    val submissionUrl: String,
    val submissionNotes: String? = null
)

// Submit work response
data class SubmitWorkResponse(
    val success: Boolean,
    val message: String,
    val data: BountyItem
)

// Applicant with submission details
data class Applicant(
    val userId: Int,
    val username: String?,
    val email: String,
    val claimedAt: String,
    val submissionUrl: String?,
    val submissionNotes: String?,
    val submittedAt: String?,
    val isWinner: Boolean
)

// Get applicants response
data class ApplicantsResponse(
    val success: Boolean,
    val data: List<Applicant>
)

// Select winner response
data class SelectWinnerResponse(
    val success: Boolean,
    val message: String,
    val data: WinnerData
)

data class WinnerData(
    val bountyId: String,
    val winnerId: Int,
    val xpAwarded: Int,
    val moneyAwarded: Int,
    val completedAt: String
)

