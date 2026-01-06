package com.jason.alp_vp.data.service

import com.jason.alp_vp.ui.model.Bounty
import retrofit2.Response
import retrofit2.http.*

interface BountyService {

    // 1. Get All Bounties - AUTH via interceptor
    @GET("bounties")
    suspend fun getAllBounties(): Response<BountiesResponse>

    // 2. Get Bounty by ID - AUTH via interceptor
    @GET("bounties/{id}")
    suspend fun getBountyById(
        @Path("id") id: String
    ): Response<BountyDetailResponse>

    // 3. Create Bounty (Company only) - AUTH via interceptor
    @POST("bounties")
    suspend fun createBounty(
        @Body request: BountyCreateDto
    ): Response<BountyDetailResponse>

    // 4. Claim Bounty (User) - AUTH via interceptor
    @POST("bounties/{id}/claim")
    suspend fun claimBounty(
        @Path("id") bountyId: String
    ): Response<AssignBountyResponse>

    // 5. Unclaim Bounty (User) - AUTH via interceptor
    @DELETE("bounties/{id}/unclaim")
    suspend fun unclaimBounty(
        @Path("id") bountyId: String
    ): Response<AssignBountyResponse>

    // 6. Submit Work (User only) - AUTH via interceptor
    @POST("bounties/{id}/submit")
    suspend fun submitBounty(
        @Path("id") bountyId: String,
        @Body request: BountySubmitDto
    ): Response<SubmitBountyResponse>

    // 7. Get My Bounties (User) - AUTH via interceptor
    @GET("my-bounties")
    suspend fun getMyBounties(): Response<MyBountiesResponse>

    // 8. Select Winner (Company) - AUTH via interceptor
    @POST("bounties/{id}/select-winner/{userId}")
    suspend fun selectWinner(
        @Path("id") bountyId: String,
        @Path("userId") userId: String
    ): Response<BountyDetailResponse>

    // 9. Get Company Bounties - AUTH via interceptor
    @GET("company/my-bounties")
    suspend fun getCompanyBounties(): Response<BountiesResponse>

    // 10. Get Bounty Applicants (Company) - AUTH via interceptor
    @GET("bounties/{id}/applicants")
    suspend fun getBountyApplicants(
        @Path("id") bountyId: String
    ): Response<BountyApplicantsResponse>

    // 11. Update Bounty (Company) - AUTH via interceptor
    @PUT("bounties/{id}")
    suspend fun updateBounty(
        @Path("id") id: String,
        @Body request: UpdateBountyRequest
    ): Response<BountyDetailResponse>

    // 12. Delete Bounty (Company only) - AUTH via interceptor
    @DELETE("bounties/{id}")
    suspend fun deleteBounty(
        @Path("id") id: String
    ): Response<DeleteBountyResponse>
}

// ============= REQUEST DTOs =============

data class BountyCreateDto(
    val title: String,
    val company_id: Int,
    val description: String? = null,
    val deadline: String, // ISO 8601 format
    val rewardXp: Int,
    val rewardMoney: Int,
    val status: String = "open" // open, in_progress, completed, cancelled
)

data class BountySubmitDto(
    val user_id: Int,
    val bounty_id: String,
    val is_completed: Boolean = true,
    val submission_url: String? = null,
    val submission_notes: String? = null
)


data class UpdateBountyRequest(
    val title: String? = null,
    val description: String? = null,
    val deadline: String? = null,
    val rewardXp: Int? = null,
    val rewardMoney: Int? = null,
    val status: String? = null, // open, in_progress, completed, cancelled
    val winner_id: Int? = null
)

// ============= RESPONSE DTOs =============

data class BountiesResponse(
    val success: Boolean,
    val data: List<BountyItem>
)

data class BountyDetailResponse(
    val success: Boolean,
    val data: BountyItem
)

data class BountyItem(
    val id: String,
    val title: String,
    val company_id: Int,
    val description: String?,
    val deadline: String,
    val rewardXp: Int,
    val rewardMoney: Int,
    val status: String,
    val created_at: String,
    val winner_id: Int?,

    // Relations (populated by backend)
    val company: BountyCompanyInfo?,
    val winner: UserInfo?,
    val assignments: List<BountyAssignmentItem>? = null
)

data class BountyCompanyInfo(
    val id: Int,
    val name: String,
    val email: String?,
    val logo: String?
)

data class UserInfo(
    val id: Int,
    val username: String,
    val email: String,
    val profile_image: String?
)

data class BountyAssignmentItem(
    val user_id: Int,
    val bounty_id: String,
    val assigned_at: String,
    val is_completed: Boolean,
    val completed_at: String?,
    val submission_url: String?,
    val submission_notes: String?,

    // User relation
    val user: UserInfo?
)

data class AssignBountyResponse(
    val success: Boolean,
    val message: String,
    val data: BountyAssignmentItem
)

data class SubmitBountyResponse(
    val success: Boolean,
    val message: String,
    val data: BountyAssignmentItem
)

data class MyBountiesResponse(
    val success: Boolean,
    val data: List<MyBountyItem>
)

data class BountyApplicantsResponse(
    val success: Boolean,
    val data: List<BountyAssignmentItem>
)

data class MyBountyItem(
    val bounty_id: String,
    val title: String,
    val company_id: Int,
    val description: String?,
    val deadline: String,
    val rewardXp: Int,
    val rewardMoney: Int,
    val status: String,
    val assigned_at: String,
    val is_completed: Boolean,
    val completed_at: String?,
    val submission_url: String?,
    val submission_notes: String?,

    // Relations
    val company: BountyCompanyInfo?
)

data class DeleteBountyResponse(
    val success: Boolean,
    val message: String
)

// ============= UI MODEL MAPPING =============

fun BountyItem.toUiModel(): Bounty {
    return Bounty(
        id = this.id,
        title = this.title,
        company = this.company?.name ?: "Unknown Company",
        deadline = this.deadline,
        rewardXp = this.rewardXp,
        rewardMoney = this.rewardMoney,
        status = this.status,
        claimedBy = this.winner?.username,
        assignedAt = this.assignments?.firstOrNull()?.assigned_at,
        isCompleted = this.assignments?.firstOrNull()?.is_completed ?: false
    )
}

fun MyBountyItem.toUiModel(): Bounty {
    return Bounty(
        id = this.bounty_id,
        title = this.title,
        company = this.company?.name ?: "Unknown Company",
        deadline = this.deadline,
        rewardXp = this.rewardXp,
        rewardMoney = this.rewardMoney,
        status = this.status,
        claimedBy = null,
        assignedAt = this.assigned_at,
        isCompleted = this.is_completed
    )
}
