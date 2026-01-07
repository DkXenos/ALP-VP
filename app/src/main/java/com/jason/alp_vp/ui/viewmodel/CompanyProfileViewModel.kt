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
                if (userData == null) {
                    _errorState.value = "Please login first"
                    return@launch
                }

                // Load company profile from backend
                val company = container.companyRepository.getCompanyById(userData.id.toString())
                _companyProfile.value = company
                _walletBalance.value = company.walletBalance
                _followersCount.value = company.followersCount
                _followingCount.value = company.followingCount
                _followedPagesCount.value = company.followedPagesCount

                // Skip bounty loading for now since it's not pure bounty-only
                _activeBounties.value = emptyList()
                _expiredBounties.value = emptyList()

            } catch (e: Exception) {
                Log.e("CompanyProfileVM", "Error loading company data", e)
                _errorState.value = "Failed to load company data: ${e.message}"

                // Set fallback sample data
                _companyProfile.value = Company(
                    id = 1,
                    name = "Sample Company",
                    email = "company@example.com",
                    description = "Sample company description",
                    logo = null,
                    createdAt = "2024-01-01",
                    walletBalance = 10000.0,
                    followersCount = 150,
                    followingCount = 25,
                    followedPagesCount = 10
                )
                _walletBalance.value = 10000.0
                _followersCount.value = 150
                _followingCount.value = 25
                _followedPagesCount.value = 10
            } finally {
                _loadingState.value = false
            }
        }
    }

    // Profile image management
    fun uploadProfileImage(imageFile: java.io.File) {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null

            try {
                val logoUrl = container.companyRepository.uploadCompanyLogo(imageFile)
                _companyProfile.value = _companyProfile.value?.copy(logo = logoUrl)
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
                container.companyRepository.deleteCompanyLogo()
                _companyProfile.value = _companyProfile.value?.copy(logo = null)
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
                // TokenManager.clearUserData() // Method might not exist yet
            } catch (e: Exception) {
                Log.e("CompanyProfileVM", "Error during logout", e)
            }
        }
    }

    fun withdrawMoney(amount: Double, paymentMethodId: String) {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null

            try {
                // TODO: Implement withdrawal API call when backend supports it
                // For now, simulate withdrawal
                if (amount <= _walletBalance.value) {
                    _walletBalance.value = _walletBalance.value - amount

                    // Add transaction to history
                    val transaction = WalletTransaction(
                        id = "txn_${System.currentTimeMillis()}",
                        amount = -amount,
                        type = "withdrawal",
                        description = "Withdrawal to payment method",
                        createdAt = java.time.Instant.now(),
                        status = "completed"
                    )
                    _walletHistory.value = _walletHistory.value + transaction
                } else {
                    _errorState.value = "Insufficient balance"
                }
            } catch (e: Exception) {
                Log.e("CompanyProfileVM", "Error withdrawing money", e)
                _errorState.value = "Failed to withdraw money: ${e.message}"
            } finally {
                _loadingState.value = false
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

