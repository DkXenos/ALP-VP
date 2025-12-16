package com.jason.alp_vp.data.repository

import com.jason.alp_vp.data.service.CompanyService
import com.jason.alp_vp.ui.model.Company as UiCompany
import com.jason.alp_vp.data.service.CompanyRegisterRequest
import com.jason.alp_vp.data.service.CompanyResponse
import com.jason.alp_vp.data.service.UpdateCompanyRequest

class CompanyRepository(private val service: CompanyService) {

    suspend fun register(name: String, email: String, password: String, description: String? = null): Pair<String, UiCompany> {
        val req = CompanyRegisterRequest(
            name = name,
            email = email,
            password = password,
            description = description
        )
        val response = service.register(req)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to register company: ${response.code()} - $msg")
        }
        val body = response.body()!!
        // CompanyAuthResponse (from service) has token and company
        return Pair(body.token, mapToUi(body.company))
    }

    suspend fun login(email: String, password: String): Pair<String, UiCompany> {
        val req = com.jason.alp_vp.data.service.CompanyLoginRequest(email = email, password = password)
        val response = service.login(req)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to login company: ${response.code()} - $msg")
        }
        val body = response.body()!!
        return Pair(body.token, mapToUi(body.company))
    }

    suspend fun getAllCompanies(): List<UiCompany> {
        val response = service.getAllCompanies()
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to fetch companies: ${response.code()} - $msg")
        }
        val body = response.body()!! // List<service.CompanyResponse>
        return body.map { mapToUi(it) }
    }

    suspend fun getCompanyById(id: String): UiCompany {
        val response = service.getCompanyById(id)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to fetch company: ${response.code()} - $msg")
        }
        val body = response.body()!!
        return mapToUi(body)
    }

    suspend fun updateCompany(id: String, name: String? = null, description: String? = null, logo: String? = null): UiCompany {
        val req = UpdateCompanyRequest(
            name = name,
            description = description,
            logo = logo
        )
        val response = service.updateCompany(id, req)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to update company: ${response.code()} - $msg")
        }
        val body = response.body()!!
        return mapToUi(body)
    }

    suspend fun deleteCompany(id: String) {
        val response = service.deleteCompany(id)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to delete company: ${response.code()} - $msg")
        }
    }

    private fun mapToUi(item: CompanyResponse): UiCompany {
        val idInt = try { item.id.toInt() } catch (_: Exception) { 0 }
        return UiCompany(
            id = idInt,
            name = item.name,
            email = item.email,
            description = item.description ?: "",
            logo = item.logo,
            createdAt = "",
            walletBalance = item.walletBalance,
            followersCount = item.followersCount,
            followingCount = item.followingCount,
            followedPagesCount = item.followedPagesCount
        )
    }
}
