package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
import com.jason.alp_vp.data.service.Applicant
import com.jason.alp_vp.data.service.BountyItem
import com.jason.alp_vp.data.service.BountySubmitDto
import com.jason.alp_vp.data.service.BountyAssignDto
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
                // Gunakan assignBounty sebagai alternatif untuk claimBounty
                val request = BountyAssignDto()
                val response = bountyService.assignBounty(bountyId, request)
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

                // Use bountyRepository to submit bounty
                container.bountyRepository.submitBounty(bountyId, userId, submissionUrl, submissionNotes)
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
                // Gunakan getMyClaimedBounties sebagai alternatif karena getBountyApplicants tidak tersedia
                // Ini akan memberikan daftar bounty yang diklaim oleh user
                val response = bountyService.getMyClaimedBounties()

                if (response.isSuccessful && response.body() != null) {
                    // Filter untuk bounty yang dimaksud dan convert ke Applicant list
                    val claimedBounties = response.body()!!.data
                    val applicantsFromBounties = claimedBounties
                        .filter { it.bounty_id == bountyId }
                        .map { bountyItem ->
                            Applicant(
                                userId = bountyItem.company_id, // Gunakan company_id sebagai userId sementara
                                username = null,
                                email = "user@example.com", // Default email
                                claimedAt = bountyItem.assigned_at,
                                submissionUrl = bountyItem.submission_url,
                                submissionNotes = bountyItem.submission_notes,
                                submittedAt = bountyItem.completed_at,
                                isWinner = bountyItem.is_completed
                            )
                        }

                    _applicants.value = applicantsFromBounties
                    Log.d("BountyDetailViewModel", "Applicants loaded: ${applicantsFromBounties.size}")
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
                container.bountyRepository.selectWinner(bountyId, userId)
                Log.d("BountyDetailViewModel", "Winner selected successfully")
                _winnerSelected.value = true
                // Reload bounty and applicants to reflect winner status
                loadBountyDetail(bountyId)
                loadApplicants(bountyId)
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

