package com.jason.alp_vp.network

import com.jason.alp_vp.ui.model.Bounty
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("bounties")
    suspend fun getBounties(
        @Header("Authorization") authorization: String = "Bearer test-token"
    ): Response<List<Bounty>>
}
