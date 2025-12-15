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
                    id = "comp_1",
                    name = "TechCorp Solutions",
                    description = "Leading technology company specializing in innovative mobile solutions and digital transformation. We help businesses scale through cutting-edge software development and provide comprehensive digital solutions for modern enterprises. Our team of expert developers creates scalable, secure, and user-friendly applications.",
                    logo = null,
                    walletBalance = 15250.75,
                    followersCount = 1247,
                    followingCount = 89,
                    followedPagesCount = 23
                )

                val activeBountiesData = listOf(
                    Bounty(
                        id = "bounty_1",
                        title = "Mobile App UI/UX Design",
                        description = "Create modern mobile app interface for e-commerce platform with dark theme and smooth animations",
                        reward = 2500.0,
                        difficulty = "Medium",
                        minLevelRequired = 3,
                        type = "bounty",
                        status = "active",
                        createdAt = Instant.now().minus(2, ChronoUnit.DAYS),
                        deadline = Instant.now().plus(5, ChronoUnit.DAYS)
                    ),
                    Bounty(
                        id = "bounty_2",
                        title = "Android Kotlin Development",
                        description = "Develop native Android application with MVVM architecture and Jetpack Compose",
                        reward = 3500.0,
                        difficulty = "Hard",
                        minLevelRequired = 5,
                        type = "bounty",
                        status = "active",
                        createdAt = Instant.now().minus(1, ChronoUnit.DAYS),
                        deadline = Instant.now().plus(7, ChronoUnit.DAYS)
                    ),
                    Bounty(
                        id = "event_1",
                        title = "Tech Conference 2024",
                        description = "Annual technology conference with industry leaders and networking opportunities",
                        reward = 0.0,
                        difficulty = "Easy",
                        minLevelRequired = 1,
                        type = "event",
                        status = "active",
                        createdAt = Instant.now().minus(1, ChronoUnit.DAYS),
                        deadline = Instant.now().plus(10, ChronoUnit.DAYS),
                        quota = 100,
                        countdownEnd = Instant.now().plus(10, ChronoUnit.DAYS)
                    ),
                    Bounty(
                        id = "event_2",
                        title = "Mobile Dev Workshop",
                        description = "Hands-on workshop for mobile development with React Native and Flutter",
                        reward = 0.0,
                        difficulty = "Medium",
                        minLevelRequired = 2,
                        type = "event",
                        status = "active",
                        createdAt = Instant.now().minus(3, ChronoUnit.DAYS),
                        deadline = Instant.now().plus(14, ChronoUnit.DAYS),
                        quota = 50,
                        countdownEnd = Instant.now().plus(14, ChronoUnit.DAYS)
                    )
                )

                val expiredBountiesData = listOf(
                    Bounty(
                        id = "bounty_exp_1",
                        title = "Backend API Development",
                        description = "REST API development for mobile application with Node.js and MongoDB",
                        reward = 1800.0,
                        difficulty = "Hard",
                        minLevelRequired = 5,
                        type = "bounty",
                        status = "expired",
                        createdAt = Instant.now().minus(30, ChronoUnit.DAYS),
                        deadline = Instant.now().minus(5, ChronoUnit.DAYS)
                    ),
                    Bounty(
                        id = "bounty_exp_2",
                        title = "Web Scraping Project",
                        description = "Create automated web scraping tool with Python and Beautiful Soup",
                        reward = 1200.0,
                        difficulty = "Medium",
                        minLevelRequired = 3,
                        type = "bounty",
                        status = "expired",
                        createdAt = Instant.now().minus(20, ChronoUnit.DAYS),
                        deadline = Instant.now().minus(2, ChronoUnit.DAYS)
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
