package com.jason.alp_vp.data.repository

import com.jason.alp_vp.data.DummyDataRepository
import com.jason.alp_vp.data.api.ApiService
import com.jason.alp_vp.data.api.Result
import com.jason.alp_vp.data.api.RetrofitClient
import com.jason.alp_vp.data.api.model.ApplicationRequest
import com.jason.alp_vp.data.api.toResult
import com.jason.alp_vp.ui.model.Bounty
import com.jason.alp_vp.ui.model.toBounty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for Bounty data
 * Handles data from both API (when available) and local dummy data (fallback)
 *
 * Set USE_API_DATA = true when your backend is ready
 */
class BountyRepository(
    private val apiService: ApiService = RetrofitClient.getApiService()
) {
    companion object {
        // TODO: Set to true when backend is ready
        private const val USE_API_DATA = true

        @Volatile
        private var instance: BountyRepository? = null

        fun getInstance(): BountyRepository {
            return instance ?: synchronized(this) {
                instance ?: BountyRepository().also { instance = it }
            }
        }
    }

    /**
     * Get all bounties
     */
    suspend fun getAllBounties(): Result<List<Bounty>> = withContext(Dispatchers.IO) {
        try {
            if (USE_API_DATA) {
                // Use API
                val response = apiService.getAllBounties()

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    val bounties = apiResponse.data?.map { it.toBounty() } ?: emptyList()
                    Result.Success(bounties)
                } else {
                    Result.Error(Exception("API Error: ${response.code()}"), response.message())
                }
            } else {
                // Use dummy data as fallback
                Result.Success(DummyDataRepository.getAllBounties())
            }
        } catch (e: Exception) {
            Result.Error(e, "Failed to fetch bounties: ${e.message}")
        }
    }

    /**
     * Get bounty by ID
     */
    suspend fun getBountyById(bountyId: String): Result<Bounty> = withContext(Dispatchers.IO) {
        try {
            if (USE_API_DATA) {
                // Use API
                val response = apiService.getBountyById(bountyId)

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    val bounty = apiResponse.data?.toBounty()
                    if (bounty != null) {
                        Result.Success(bounty)
                    } else {
                        Result.Error(Exception("Bounty not found"))
                    }
                } else {
                    Result.Error(Exception("API Error: ${response.code()}"), response.message())
                }
            } else {
                // Use dummy data as fallback
                val bounty = DummyDataRepository.getBountyById(bountyId)
                if (bounty != null) {
                    // Merge with details
                    val details = DummyDataRepository.getBountyDetails(bountyId)
                    val fullBounty = bounty.copy(
                        description = details?.description,
                        requirements = details?.requirements
                    )
                    Result.Success(fullBounty)
                } else {
                    Result.Error(Exception("Bounty not found"))
                }
            }
        } catch (e: Exception) {
            Result.Error(e, "Failed to fetch bounty: ${e.message}")
        }
    }

    /**
     * Search bounties by query
     */
    suspend fun searchBounties(query: String): Result<List<Bounty>> = withContext(Dispatchers.IO) {
        try {
            if (USE_API_DATA) {
                // Use API
                val response = apiService.searchBounties(query)

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    val bounties = apiResponse.data?.map { it.toBounty() } ?: emptyList()
                    Result.Success(bounties)
                } else {
                    Result.Error(Exception("API Error: ${response.code()}"), response.message())
                }
            } else {
                // Use dummy data as fallback
                Result.Success(DummyDataRepository.searchBounties(query))
            }
        } catch (e: Exception) {
            Result.Error(e, "Failed to search bounties: ${e.message}")
        }
    }

    /**
     * Filter bounties by type
     */
    suspend fun filterBounties(type: String? = null, status: String? = null): Result<List<Bounty>> =
        withContext(Dispatchers.IO) {
            try {
                if (USE_API_DATA) {
                    // Use API
                    val response = apiService.filterBounties(type, status)

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!
                        val bounties = apiResponse.data?.map { it.toBounty() } ?: emptyList()
                        Result.Success(bounties)
                    } else {
                        Result.Error(Exception("API Error: ${response.code()}"), response.message())
                    }
                } else {
                    // Use dummy data as fallback
                    Result.Success(DummyDataRepository.filterBountiesByType(type ?: "all"))
                }
            } catch (e: Exception) {
                Result.Error(e, "Failed to filter bounties: ${e.message}")
            }
        }

    /**
     * Submit application for a bounty
     */
    suspend fun submitApplication(
        bountyId: String,
        portfolioLinks: List<String>,
        cvImageUrl: String,
        whyHireYou: String
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            if (USE_API_DATA) {
                // Use API
                val request = ApplicationRequest(
                    bountyId = bountyId,
                    portfolioLinks = portfolioLinks,
                    cvImageUrl = cvImageUrl,
                    whyHireYou = whyHireYou
                )

                val response = apiService.submitApplication(bountyId, request)

                if (response.isSuccessful) {
                    Result.Success(true)
                } else {
                    Result.Error(Exception("API Error: ${response.code()}"), response.message())
                }
            } else {
                // Use dummy data as fallback
                val success = DummyDataRepository.submitApplication(
                    bountyId, portfolioLinks, cvImageUrl, whyHireYou
                )
                Result.Success(success)
            }
        } catch (e: Exception) {
            Result.Error(e, "Failed to submit application: ${e.message}")
        }
    }
}

