package com.jason.alp_vp.data.service

import retrofit2.Response
import retrofit2.http.*

interface CompanyService {

    @POST("/companies/register")
    suspend fun register(
        @Body company: CompanyRegisterRequest
    ): Response<CompanyAuthResponse>

    @POST("/companies/login")
    suspend fun login(
        @Body credentials: CompanyLoginRequest
    ): Response<CompanyAuthResponse>

    @GET("/companies")
    suspend fun getAllCompanies(): Response<List<CompanyResponse>>

    @GET("/companies/{id}")
    suspend fun getCompanyById(
        @Path("id") id: String
    ): Response<CompanyResponse>

    @PUT("/companies/{id}")
    suspend fun updateCompany(
        @Path("id") id: String,
        @Body company: UpdateCompanyRequest
    ): Response<CompanyResponse>

    @DELETE("/companies/{id}")
    suspend fun deleteCompany(
        @Path("id") id: String
    ): Response<Unit>
}

data class CompanyRegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val description: String? = null
)

data class CompanyLoginRequest(
    val email: String,
    val password: String
)

data class CompanyAuthResponse(
    val token: String,
    val company: CompanyResponse
)

data class CompanyResponse(
    val id: String,
    val name: String,
    val email: String,
    val description: String?,
    val logo: String?,
    val walletBalance: Double,
    val followersCount: Int,
    val followingCount: Int,
    val followedPagesCount: Int
)

data class UpdateCompanyRequest(
    val name: String? = null,
    val description: String? = null,
    val logo: String? = null
)

