package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
import com.jason.alp_vp.data.service.Applicant
import com.jason.alp_vp.data.service.BountyItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BountyDetailViewModel(
    private val container: AppContainer = AppContainer()
) : ViewModel() {

    // ✅ CORRECT: Use repository instead of service
    private val bountyRepository = container.bountyRepository

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
<<<<<<< Updated upstream
                // ✅ Use repository - it handles error checking and validation
=======
                // ✅ Use repository
>>>>>>> Stashed changes
                val response = bountyRepository.getBountyById(bountyId)
                _bountyDetail.value = response.data
                Log.d("BountyDetailViewModel", "Bounty loaded: ${response.data}")
            } catch (e: Exception) {
<<<<<<< Updated upstream
                // Repository provides user-friendly error messages
=======
>>>>>>> Stashed changes
                val errorMsg = e.message ?: "Error loading bounty"
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
<<<<<<< Updated upstream
                // ✅ Use repository - it validates input and provides user-friendly errors
=======
                // ✅ Use repository
>>>>>>> Stashed changes
                bountyRepository.claimBounty(bountyId)
                Log.d("BountyDetailViewModel", "Bounty claimed successfully")
                _claimSuccess.value = true
                // Reload bounty to get updated status
                loadBountyDetail(bountyId)
            } catch (e: Exception) {
<<<<<<< Updated upstream
                // Repository provides user-friendly error messages like "This bounty is already claimed"
=======
>>>>>>> Stashed changes
                val errorMsg = e.message ?: "Error claiming bounty"
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
<<<<<<< Updated upstream
                // ✅ Use repository - it validates URL format and provides user-friendly errors
=======
                // ✅ Use repository - it validates URL format
>>>>>>> Stashed changes
                bountyRepository.submitWork(bountyId, submissionUrl, submissionNotes)
                Log.d("BountyDetailViewModel", "Work submitted successfully")
                _submitSuccess.value = true
                // Reload bounty to get updated submission status
                loadBountyDetail(bountyId)
            } catch (e: IllegalArgumentException) {
<<<<<<< Updated upstream
                // Validation error (e.g., invalid URL format)
=======
                // Validation error
>>>>>>> Stashed changes
                val errorMsg = e.message ?: "Invalid submission"
                Log.e("BountyDetailViewModel", "Validation error: $errorMsg", e)
                _error.value = errorMsg
            } catch (e: Exception) {
                // Network error
                val errorMsg = e.message ?: "Error submitting work"
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
<<<<<<< Updated upstream
                // ✅ Use repository - it provides user-friendly error for non-companies
=======
                // ✅ Use repository
>>>>>>> Stashed changes
                val response = bountyRepository.getBountyApplicants(bountyId)
                _applicants.value = response.data
                Log.d("BountyDetailViewModel", "Applicants loaded: ${response.data.size}")
            } catch (e: Exception) {
<<<<<<< Updated upstream
                // Repository provides user-friendly error messages like "Only companies can view applicants"
=======
>>>>>>> Stashed changes
                val errorMsg = e.message ?: "Error loading applicants"
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
<<<<<<< Updated upstream
                // ✅ Use repository - it validates input and provides user-friendly errors
=======
                // ✅ Use repository
>>>>>>> Stashed changes
                bountyRepository.selectWinner(bountyId, userId)
                Log.d("BountyDetailViewModel", "Winner selected successfully")
                _winnerSelected.value = true
                // Reload bounty and applicants to reflect winner status
                loadBountyDetail(bountyId)
                loadApplicants(bountyId)
            } catch (e: Exception) {
<<<<<<< Updated upstream
                // Repository provides user-friendly error messages like "Only companies can select winners"
=======
>>>>>>> Stashed changes
                val errorMsg = e.message ?: "Error selecting winner"
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

