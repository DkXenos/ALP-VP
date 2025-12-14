package com.jason.alp_vp.repository

import com.jason.alp_vp.network.RetrofitInstance
import com.jason.alp_vp.ui.model.Bounty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BountyRepository {
    private val api = RetrofitInstance.api

    suspend fun getBounties(): Result<List<Bounty>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getBounties()
                if (response.isSuccessful) {
                    Result.success(response.body() ?: emptyList())
                } else {
                    Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
