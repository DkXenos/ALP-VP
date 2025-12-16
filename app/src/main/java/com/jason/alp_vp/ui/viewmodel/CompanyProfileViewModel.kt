package com.jason.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.ui.model.Bounty
import com.jason.alp_vp.ui.model.Company
import com.jason.alp_vp.ui.model.PaymentMethod
import com.jason.alp_vp.ui.model.WalletTransaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit

class CompanyProfileViewModel : ViewModel() {

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

            try {
                // Dummy data - replace with actual repository calls
                val company = Company(
                    id = 1,
                    name = "TechCorp Solutions",
                    email = "contact@techcorp.com",
                    description = "Leading technology company specializing in innovative mobile solutions and digital transformation. We help businesses scale through cutting-edge software development and provide comprehensive digital solutions for modern enterprises. Our team of expert developers creates scalable, secure, and user-friendly applications.",
                    logo = null,
                    createdAt = "2024-01-01T00:00:00Z",
                    walletBalance = 15250.75,
                    followersCount = 1247,
                    followingCount = 89,
                    followedPagesCount = 23
                )

                val activeBountiesData = listOf(
                    Bounty(
                        id = "bounty_1",
                        title = "Mobile App UI/UX Design",
                        company = "TechCorp Solutions",
                        deadline = "2024-12-21T23:59:59Z",
                        rewardXp = 250,
                        rewardMoney = 2500,
                        status = "active"
                    ),
                    Bounty(
                        id = "bounty_2",
                        title = "Android Kotlin Development",
                        company = "TechCorp Solutions",
                        deadline = "2024-12-23T23:59:59Z",
                        rewardXp = 350,
                        rewardMoney = 3500,
                        status = "active"
                    ),
                    Bounty(
                        id = "event_1",
                        title = "Tech Conference 2024",
                        company = "TechCorp Solutions",
                        deadline = "2024-12-26T23:59:59Z",
                        rewardXp = 100,
                        rewardMoney = 0,
                        status = "active"
                    ),
                    Bounty(
                        id = "event_2",
                        title = "Mobile Dev Workshop",
                        company = "TechCorp Solutions",
                        deadline = "2024-12-30T23:59:59Z",
                        rewardXp = 200,
                        rewardMoney = 0,
                        status = "active"
                    )
                )

                val expiredBountiesData = listOf(
                    Bounty(
                        id = "bounty_exp_1",
                        title = "Backend API Development",
                        company = "TechCorp Solutions",
                        deadline = "2024-11-11T23:59:59Z",
                        rewardXp = 180,
                        rewardMoney = 1800,
                        status = "expired"
                    ),
                    Bounty(
                        id = "bounty_exp_2",
                        title = "Web Scraping Project",
                        company = "TechCorp Solutions",
                        deadline = "2024-11-14T23:59:59Z",
                        rewardXp = 120,
                        rewardMoney = 1200,
                        status = "expired"
                    )
                )

                val walletTransactions = listOf(
                    WalletTransaction(
                        id = "txn_1",
                        amount = 2500.0,
                        type = "earned",
                        description = "Mobile App UI/UX Design - Bounty completion reward",
                        status = "completed",
                        createdAt = Instant.now().minus(3, ChronoUnit.DAYS)
                    ),
                    WalletTransaction(
                        id = "txn_2",
                        amount = 3500.0,
                        type = "earned",
                        description = "Android Development Project - Bounty completion reward",
                        status = "completed",
                        createdAt = Instant.now().minus(5, ChronoUnit.DAYS)
                    ),
                    WalletTransaction(
                        id = "txn_3",
                        amount = -1000.0,
                        type = "withdrawn",
                        description = "Withdrawal to Bank Central Asia",
                        status = "completed",
                        createdAt = Instant.now().minus(7, ChronoUnit.DAYS)
                    ),
                    WalletTransaction(
                        id = "txn_4",
                        amount = 1800.0,
                        type = "earned",
                        description = "Backend API Development - Bounty completion reward",
                        status = "completed",
                        createdAt = Instant.now().minus(10, ChronoUnit.DAYS)
                    ),
                    WalletTransaction(
                        id = "txn_5",
                        amount = -500.0,
                        type = "withdrawn",
                        description = "Withdrawal to GoPay e-wallet",
                        status = "completed",
                        createdAt = Instant.now().minus(15, ChronoUnit.DAYS)
                    )
                )

                val paymentMethodsData = listOf(
                    PaymentMethod(
                        id = "pm_1",
                        type = "bank",
                        name = "Bank Central Asia",
                        accountNumber = "****1234",
                        isDefault = true
                    ),
                    PaymentMethod(
                        id = "pm_2",
                        type = "ewallet",
                        name = "GoPay",
                        accountNumber = "081234567890",
                        isDefault = false
                    ),
                    PaymentMethod(
                        id = "pm_3",
                        type = "bank",
                        name = "Bank Mandiri",
                        accountNumber = "****5678",
                        isDefault = false
                    ),
                    PaymentMethod(
                        id = "pm_4",
                        type = "ewallet",
                        name = "OVO",
                        accountNumber = "081987654321",
                        isDefault = false
                    )
                )

                // Set all the state values
                _companyProfile.value = company
                _walletBalance.value = company.walletBalance
                _activeBounties.value = activeBountiesData
                _expiredBounties.value = expiredBountiesData
                _followersCount.value = company.followersCount
                _followingCount.value = company.followingCount
                _followedPagesCount.value = company.followedPagesCount
                _walletHistory.value = walletTransactions
                _paymentMethods.value = paymentMethodsData

            } catch (e: Exception) {
                _errorState.value = "Failed to load company data: ${e.message}"
            } finally {
                _loadingState.value = false
            }
        }
    }

    fun withdrawMoney(amount: Double, paymentMethodId: String) {
        viewModelScope.launch {
            _loadingState.value = true

            try {
                // Validate withdrawal
                if (amount <= 0) {
                    _errorState.value = "Invalid withdrawal amount"
                    return@launch
                }

                if (amount > _walletBalance.value) {
                    _errorState.value = "Insufficient balance"
                    return@launch
                }

                val paymentMethod = _paymentMethods.value.find { it.id == paymentMethodId }
                if (paymentMethod == null) {
                    _errorState.value = "Payment method not found"
                    return@launch
                }

                // Simulate withdrawal process
                val newBalance = _walletBalance.value - amount
                _walletBalance.value = newBalance

                // Update company profile balance
                _companyProfile.value?.let { company ->
                    _companyProfile.value = company.copy(walletBalance = newBalance)
                }

                // Add new transaction
                val newTransaction = WalletTransaction(
                    id = "txn_${System.currentTimeMillis()}",
                    amount = -amount,
                    type = "withdrawn",
                    description = "Withdrawal to ${paymentMethod.name}",
                    status = "pending",
                    createdAt = Instant.now()
                )

                _walletHistory.value = listOf(newTransaction) + _walletHistory.value

                // Show success message
                _errorState.value = null

            } catch (e: Exception) {
                _errorState.value = "Withdrawal failed: ${e.message}"
            } finally {
                _loadingState.value = false
            }
        }
    }

    fun addPaymentMethod(paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            try {
                _paymentMethods.value = _paymentMethods.value + paymentMethod
            } catch (e: Exception) {
                _errorState.value = "Failed to add payment method: ${e.message}"
            }
        }
    }

    fun removePaymentMethod(paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            try {
                if (paymentMethod.isDefault) {
                    _errorState.value = "Cannot remove default payment method"
                    return@launch
                }

                _paymentMethods.value = _paymentMethods.value.filter { it.id != paymentMethod.id }
            } catch (e: Exception) {
                _errorState.value = "Failed to remove payment method: ${e.message}"
            }
        }
    }

    fun refreshData() {
        loadCompanyData()
    }

    fun clearError() {
        _errorState.value = null
    }

    fun logout() {
        viewModelScope.launch {
            try {
                // Clear all state
                _companyProfile.value = null
                _walletBalance.value = 0.0
                _activeBounties.value = emptyList()
                _expiredBounties.value = emptyList()
                _followersCount.value = 0
                _followingCount.value = 0
                _followedPagesCount.value = 0
                _walletHistory.value = emptyList()
                _paymentMethods.value = emptyList()

                // Handle logout logic (clear preferences, navigate to login, etc.)
            } catch (e: Exception) {
                _errorState.value = "Logout failed: ${e.message}"
            }
        }
    }
}
