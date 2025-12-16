package com.jason.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

enum class CreateType {
    BOUNTY,
    EVENT
}

data class BountyFormData(
    val title: String = "",
    val deadline: String = "",
    val rewardXp: String = "",
    val rewardMoney: String = "",
    val description: String = ""
)

data class EventFormData(
    val title: String = "",
    val description: String = "",
    val eventDate: String = "",
    val registeredQuota: String = ""
)

class CreatePageViewModel : ViewModel() {

    private val _selectedType = MutableStateFlow(CreateType.BOUNTY)
    val selectedType: StateFlow<CreateType> = _selectedType.asStateFlow()

    private val _bountyFormData = MutableStateFlow(BountyFormData())
    val bountyFormData: StateFlow<BountyFormData> = _bountyFormData.asStateFlow()

    private val _eventFormData = MutableStateFlow(EventFormData())
    val eventFormData: StateFlow<EventFormData> = _eventFormData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    private val _walletBalance = MutableStateFlow(15250.75) // Dummy wallet balance
    val walletBalance: StateFlow<Double> = _walletBalance.asStateFlow()

    // Mock company ID - in real app, get from auth/session
    private val currentCompanyId = "comp_1"

    fun selectType(type: CreateType) {
        _selectedType.value = type
        clearMessages()
    }

    fun updateBountyForm(
        title: String? = null,
        deadline: String? = null,
        rewardXp: String? = null,
        rewardMoney: String? = null,
        description: String? = null
    ) {
        _bountyFormData.value = _bountyFormData.value.copy(
            title = title ?: _bountyFormData.value.title,
            deadline = deadline ?: _bountyFormData.value.deadline,
            rewardXp = rewardXp ?: _bountyFormData.value.rewardXp,
            rewardMoney = rewardMoney ?: _bountyFormData.value.rewardMoney,
            description = description ?: _bountyFormData.value.description
        )
    }

    fun updateEventForm(
        title: String? = null,
        description: String? = null,
        eventDate: String? = null,
        registeredQuota: String? = null
    ) {
        _eventFormData.value = _eventFormData.value.copy(
            title = title ?: _eventFormData.value.title,
            description = description ?: _eventFormData.value.description,
            eventDate = eventDate ?: _eventFormData.value.eventDate,
            registeredQuota = registeredQuota ?: _eventFormData.value.registeredQuota
        )
    }

    fun createBounty() {
        viewModelScope.launch {
            _isLoading.value = true
            clearMessages()

            try {
                val form = _bountyFormData.value

                // Validation
                if (form.title.isBlank()) {
                    _errorMessage.value = "Title is required"
                    _isLoading.value = false
                    return@launch
                }

                if (form.deadline.isBlank()) {
                    _errorMessage.value = "Deadline is required"
                    _isLoading.value = false
                    return@launch
                }

                val rewardMoney = form.rewardMoney.toDoubleOrNull() ?: 0.0
                val rewardXp = form.rewardXp.toIntOrNull() ?: 0

                if (rewardMoney <= 0) {
                    _errorMessage.value = "Reward money must be greater than 0"
                    _isLoading.value = false
                    return@launch
                }

                if (rewardMoney > _walletBalance.value) {
                    _errorMessage.value = "Insufficient wallet balance"
                    _isLoading.value = false
                    return@launch
                }

                // Simulate API call
                kotlinx.coroutines.delay(1500)

                // Create bounty (dummy data - replace with actual API call)
                val newBounty = mapOf(
                    "id" to "bounty_${System.currentTimeMillis()}",
                    "title" to form.title,
                    "company" to currentCompanyId,
                    "deadline" to form.deadline,
                    "rewardXp" to rewardXp,
                    "rewardMoney" to rewardMoney,
                    "status" to "active",
                    "description" to form.description
                )

                // Deduct wallet balance
                _walletBalance.value -= rewardMoney

                // Reset form
                _bountyFormData.value = BountyFormData()

                _successMessage.value = "Bounty created successfully! Rp ${String.format("%,.2f", rewardMoney)} deducted from wallet."

            } catch (e: Exception) {
                _errorMessage.value = "Failed to create bounty: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createEvent() {
        viewModelScope.launch {
            _isLoading.value = true
            clearMessages()

            try {
                val form = _eventFormData.value

                // Validation
                if (form.title.isBlank()) {
                    _errorMessage.value = "Title is required"
                    _isLoading.value = false
                    return@launch
                }

                if (form.description.isBlank()) {
                    _errorMessage.value = "Description is required"
                    _isLoading.value = false
                    return@launch
                }

                if (form.eventDate.isBlank()) {
                    _errorMessage.value = "Event date is required"
                    _isLoading.value = false
                    return@launch
                }

                val quota = form.registeredQuota.toIntOrNull() ?: 0

                if (quota <= 0) {
                    _errorMessage.value = "Quota must be greater than 0"
                    _isLoading.value = false
                    return@launch
                }

                // Simulate API call
                kotlinx.coroutines.delay(1500)

                // Create event (dummy data - replace with actual API call)
                val newEvent = mapOf(
                    "id" to "event_${System.currentTimeMillis()}",
                    "title" to form.title,
                    "description" to form.description,
                    "event_date" to form.eventDate,
                    "company_id" to currentCompanyId,
                    "registered_quota" to quota
                )

                // Reset form
                _eventFormData.value = EventFormData()

                _successMessage.value = "Event created successfully!"

            } catch (e: Exception) {
                _errorMessage.value = "Failed to create event: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}

