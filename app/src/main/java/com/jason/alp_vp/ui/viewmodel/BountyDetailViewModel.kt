package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
import com.jason.alp_vp.data.service.Applicant
import com.jason.alp_vp.data.service.BountyItem
import com.jason.alp_vp.data.service.SubmitWorkRequest
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
                Log.d("BountyDetailViewModel", "Claiming bounty: $bountyId")
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
                val request = SubmitWorkRequest(submissionUrl, submissionNotes)
                val response = bountyService.submitWork(bountyId, request)

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
                    _applicants.value = response.body()!!.data
                    Log.d("BountyDetailViewModel", "Applicants loaded: ${response.body()!!.data.size}")
                } else {
                    val errorMsg = "Failed to load applicants: ${response.code()} - ${response.message()}"
                    Log.e("BountyDetailViewModel", errorMsg)
                    _error.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error loading applicants: ${e.message}"
                Log.e("BountyDetailViewModel", errorMsg, e)
                _error.value = errorMsg
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
                Log.d("BountyDetailViewModel", "Selecting winner: User $userId for bounty $bountyId")
                val response = bountyService.selectWinner(bountyId, userId)

                if (response.isSuccessful) {
                    Log.d("BountyDetailViewModel", "Winner selected successfully: ${response.body()!!.data}")
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

