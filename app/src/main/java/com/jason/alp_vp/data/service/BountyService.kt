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
}

// Response data classes
data class BountiesResponse(
    val success: Boolean,
    val data: List<BountyItem>
)

data class BountyItem(
    val id: Int,
    val title: String,
    val description: String?,
    val reward: Double,
    val status: String,
    val deadline: String?,
    val companyId: Int,
    val companyName: String?,
    val createdAt: String?,
    val claimedBy: Int?
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
        id = this.id.toString(),
        title = this.title,
        company = this.companyName ?: "Unknown Company",
        deadline = this.deadline ?: "",
        rewardXp = 0, // You can calculate or get from backend
        rewardMoney = this.reward.toInt(),
        status = this.status
    )
}

