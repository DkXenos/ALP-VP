package com.jason.alp_vp.data.api

import com.jason.alp_vp.data.api.model.ApiResponse
import com.jason.alp_vp.data.api.model.ApplicationRequest
import com.jason.alp_vp.data.api.model.ApplicationResponse
import com.jason.alp_vp.data.api.model.BountyResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit API Service Interface
 * Define all API endpoints here matching your Express backend routes
 */
interface ApiService {

    /**
     * Get all bounties
     * GET /api/bounties
     */
    @GET("bounties")
    suspend fun getAllBounties(): Response<ApiResponse<List<BountyResponse>>>

    /**
     * Get bounty by ID
     * GET /api/bounties/{id}
     */
    @GET("bounties/{id}")
    suspend fun getBountyById(
        @Path("id") bountyId: String
    ): Response<ApiResponse<BountyResponse>>

    /**
     * Search bounties
     * GET /api/bounties/search?q={query}
     */
    @GET("bounties/search")
    suspend fun searchBounties(
        @Query("q") query: String
    ): Response<ApiResponse<List<BountyResponse>>>

    /**
     * Filter bounties by type
     * GET /api/bounties?type={type}
     */
    @GET("bounties")
    suspend fun filterBounties(
        @Query("type") type: String? = null,
        @Query("status") status: String? = null
    ): Response<ApiResponse<List<BountyResponse>>>

    /**
     * Submit application for a bounty
     * POST /api/bounties/{id}/apply
     */
    @POST("bounties/{id}/apply")
    suspend fun submitApplication(
        @Path("id") bountyId: String,
        @Body application: ApplicationRequest
    ): Response<ApiResponse<ApplicationResponse>>

    /**
     * Get user's applications
     * GET /api/applications
     */
    @GET("applications")
    suspend fun getUserApplications(): Response<ApiResponse<List<ApplicationResponse>>>

    /**
     * Get application by ID
     * GET /api/applications/{id}
     */
    @GET("applications/{id}")
    suspend fun getApplicationById(
        @Path("id") applicationId: String
    ): Response<ApiResponse<ApplicationResponse>>
}

