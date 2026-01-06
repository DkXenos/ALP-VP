package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
import com.jason.alp_vp.ui.model.Bounty
import com.jason.alp_vp.ui.model.Company
import com.jason.alp_vp.ui.model.PaymentMethod
import com.jason.alp_vp.ui.model.WalletTransaction
import com.jason.alp_vp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompanyProfileViewModel(
    private val container: AppContainer = AppContainer()
) : ViewModel() {

    private val _companyProfile = MutableStateFlow<Company?>(null)
    val companyProfile: StateFlow<Company?> = _companyProfile.asStateFlow()

    private val _walletBalance = MutableStateFlow(0.0)
    val walletBalance: StateFlow<Double> = _walletBalance.asStateFlow()

    private val _activeBounties = MutableStateFlow<List<Bounty>>(emptyList())
    val activeBounties: StateFlow<List<Bounty>> = _activeBounties.asStateFlow()

    private val _expiredBounties = MutableStateFlow<List<Bounty>>(emptyList())
    val expiredBounties: StateFlow<List<Bounty>> = _expiredBounties.asStateFlow()

    private val _followersCount = MutableStateFlow(0)
    val followersCount: StateFlow<Int> = _followersCount.asStateFlow()

    private val _followingCount = MutableStateFlow(0)
    val followingCount: StateFlow<Int> = _followingCount.asStateFlow()

    private val _followedPagesCount = MutableStateFlow(0)
    val followedPagesCount: StateFlow<Int> = _followedPagesCount.asStateFlow()

    private val _walletHistory = MutableStateFlow<List<WalletTransaction>>(emptyList())
    val walletHistory: StateFlow<List<WalletTransaction>> = _walletHistory.asStateFlow()

    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethod>> = _paymentMethods.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    init {
        loadCompanyData()
    }

    private fun loadCompanyData() {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null

            try {
                // Get current user data from token
                val userData = TokenManager.getUserData()
                if (userData?.role != "COMPANY") {
                    _errorState.value = "Access denied: Not a company account"
                    return@launch
                }

                // Load company profile from backend
                val companyResponse = container.companyService.getCompanyProfile()
                if (companyResponse.isSuccessful && companyResponse.body() != null) {
                    val company = companyResponse.body()!!
                    _companyProfile.value = Company(
                        id = company.id,
                        name = company.name,
                        email = company.email,
                        description = company.description ?: "",
                        logo = company.logo,
                        createdAt = company.createdAt ?: "",
                        walletBalance = company.walletBalance ?: 0.0,
                        followersCount = company.followersCount ?: 0,
                        followingCount = company.followingCount ?: 0,
                        followedPagesCount = company.followedPagesCount ?: 0
                    )
                    _walletBalance.value = company.walletBalance ?: 0.0
                    _followersCount.value = company.followersCount ?: 0
                    _followingCount.value = company.followingCount ?: 0
                    _followedPagesCount.value = company.followedPagesCount ?: 0
                } else {
                    _errorState.value = "Failed to load company profile"
                }

                // Load company bounties from backend
                loadCompanyBounties()

            } catch (e: Exception) {
                Log.e("CompanyProfileVM", "Error loading company data", e)
                _errorState.value = "Failed to load company data: ${e.message}"
                // Set safe default values
                _companyProfile.value = null
                _walletBalance.value = 0.0
                _activeBounties.value = emptyList()
                _expiredBounties.value = emptyList()
            } finally {
                _loadingState.value = false
            }
        }
    }

    private suspend fun loadCompanyBounties() {
        try {
            val bountiesResponse = container.bountyService.getCompanyBounties()
            if (bountiesResponse.isSuccessful && bountiesResponse.body() != null) {
                val bounties = bountiesResponse.body()!!.data.map { it.toUiModel() }
                _activeBounties.value = bounties.filter { it.status == "active" }
                _expiredBounties.value = bounties.filter { it.status == "expired" }
            }
        } catch (e: Exception) {
            Log.e("CompanyProfileVM", "Error loading bounties", e)
            _activeBounties.value = emptyList()
            _expiredBounties.value = emptyList()
        }
    }

    // Profile image management
    fun uploadProfileImage(imageBytes: ByteArray) {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null

            try {
                val response = container.companyService.uploadProfileImage(imageBytes)
                if (response.isSuccessful && response.body() != null) {
                    val updatedCompany = response.body()!!
                    _companyProfile.value = _companyProfile.value?.copy(logo = updatedCompany.logo)
                } else {
                    _errorState.value = "Failed to upload profile image"
                }
            } catch (e: Exception) {
                Log.e("CompanyProfileVM", "Error uploading profile image", e)
                _errorState.value = "Failed to upload profile image: ${e.message}"
            } finally {
                _loadingState.value = false
            }
        }
    }

    fun deleteProfileImage() {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null

            try {
                val response = container.companyService.deleteProfileImage()
                if (response.isSuccessful) {
                    _companyProfile.value = _companyProfile.value?.copy(logo = null)
                } else {
                    _errorState.value = "Failed to delete profile image"
                }
            } catch (e: Exception) {
                Log.e("CompanyProfileVM", "Error deleting profile image", e)
                _errorState.value = "Failed to delete profile image: ${e.message}"
            } finally {
                _loadingState.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                TokenManager.clearToken()
                TokenManager.clearUserData()
            } catch (e: Exception) {
                Log.e("CompanyProfileVM", "Error during logout", e)
            }
        }
    }

    fun clearError() {
        _errorState.value = null
    }

    fun refreshData() {
        loadCompanyData()
    }
}

