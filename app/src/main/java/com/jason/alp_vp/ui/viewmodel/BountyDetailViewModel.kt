package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
import com.jason.alp_vp.data.service.Applicant
import com.jason.alp_vp.data.service.BountyItem
import com.jason.alp_vp.data.service.BountySubmitDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BountyDetailViewModel(
    private val container: AppContainer = AppContainer()
) : ViewModel() {

    private val bountyService = container.bountyService

    private val _bountyDetail = MutableStateFlow<BountyItem?>(null)
    val bountyDetail = _bountyDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _claimSuccess = MutableStateFlow(false)
    val claimSuccess = _claimSuccess.asStateFlow()

    private val _submitSuccess = MutableStateFlow(false)
    val submitSuccess = _submitSuccess.asStateFlow()

    private val _applicants = MutableStateFlow<List<Applicant>>(emptyList())
    val applicants = _applicants.asStateFlow()

    private val _winnerSelected = MutableStateFlow(false)
    val winnerSelected = _winnerSelected.asStateFlow()

    fun loadBountyDetail(bountyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("BountyDetailViewModel", "Loading bounty detail: $bountyId")
                val response = bountyService.getBountyById(bountyId)

                if (response.isSuccessful && response.body() != null) {
                    _bountyDetail.value = response.body()!!.data
                    Log.d("BountyDetailViewModel", "Bounty loaded: ${response.body()!!.data}")
                } else {
                    val errorMsg = "Failed to load bounty: ${response.code()} - ${response.message()}"
                    Log.e("BountyDetailViewModel", errorMsg)
                    _error.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error loading bounty: ${e.message}"
                Log.e("BountyDetailViewModel", errorMsg, e)
                _error.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun claimBounty(bountyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = bountyService.claimBounty(bountyId)

                if (response.isSuccessful) {
                    Log.d("BountyDetailViewModel", "Bounty claimed successfully")
                    _claimSuccess.value = true
                    // Reload bounty to get updated status
                    loadBountyDetail(bountyId)
                } else {
                    val errorMsg = "Failed to claim bounty: ${response.code()} - ${response.message()}"
                    Log.e("BountyDetailViewModel", errorMsg)
                    _error.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error claiming bounty: ${e.message}"
                Log.e("BountyDetailViewModel", errorMsg, e)
                _error.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitWork(bountyId: String, submissionUrl: String, submissionNotes: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("BountyDetailViewModel", "Submitting work for bounty: $bountyId")
                // Get current user ID from token
                val userData = com.jason.alp_vp.utils.TokenManager.getUserData()
                val userId = userData?.id ?: throw Exception("User not logged in")

                // Submit work via BountyService
                val request = BountySubmitDto(
                    user_id = userId,
                    bounty_id = bountyId,
                    is_completed = true,
                    submission_url = submissionUrl,
                    submission_notes = submissionNotes
                )
                val response = bountyService.submitBounty(bountyId, request)

                if (response.isSuccessful) {
                    Log.d("BountyDetailViewModel", "Work submitted successfully")
                    _submitSuccess.value = true
                    // Reload bounty to get updated submission status
                    loadBountyDetail(bountyId)
                } else {
                    val errorMsg = "Failed to submit work: ${response.code()} - ${response.message()}"
                    Log.e("BountyDetailViewModel", errorMsg)
                    _error.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error submitting work: ${e.message}"
                Log.e("BountyDetailViewModel", errorMsg, e)
                _error.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadApplicants(bountyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("BountyDetailViewModel", "Loading applicants for bounty: $bountyId")
                val response = bountyService.getBountyApplicants(bountyId)

                if (response.isSuccessful && response.body() != null) {
                    val assignments = response.body()!!.data
                    val applicantsList = assignments.map { assignment ->
                        Applicant(
                            userId = assignment.user_id,
                            username = assignment.user?.username,
                            email = assignment.user?.email ?: "",
                            claimedAt = assignment.assigned_at,
                            submissionUrl = assignment.submission_url,
                            submissionNotes = assignment.submission_notes,
                            submittedAt = assignment.completed_at,
                            isWinner = assignment.is_completed
                        )
                    }

                    _applicants.value = applicantsList
                    Log.d("BountyDetailViewModel", "Applicants loaded: ${applicantsList.size}")
                } else {
                    val errorMsg = "Failed to load applicants: ${response.code()} - ${response.message()}"
                    Log.e("BountyDetailViewModel", errorMsg)
                    _error.value = errorMsg
                    _applicants.value = emptyList()
                }
            } catch (e: Exception) {
                val errorMsg = "Error loading applicants: ${e.message}"
                Log.e("BountyDetailViewModel", errorMsg, e)
                _error.value = errorMsg
                _applicants.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectWinner(bountyId: String, userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = bountyService.selectWinner(bountyId, userId.toString())

                if (response.isSuccessful) {
                    Log.d("BountyDetailViewModel", "Winner selected successfully")
                    _winnerSelected.value = true
                    // Reload bounty and applicants to reflect winner status
                    loadBountyDetail(bountyId)
                    loadApplicants(bountyId)
                } else {
                    val errorMsg = "Failed to select winner: ${response.code()} - ${response.message()}"
                    Log.e("BountyDetailViewModel", errorMsg)
                    _error.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error selecting winner: ${e.message}"
                Log.e("BountyDetailViewModel", errorMsg, e)
                _error.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun resetClaimSuccess() {
        _claimSuccess.value = false
    }

    fun resetSubmitSuccess() {
        _submitSuccess.value = false
    }

    fun resetWinnerSelected() {
        _winnerSelected.value = false
    }
}

