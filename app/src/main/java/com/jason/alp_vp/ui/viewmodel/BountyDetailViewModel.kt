package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.service.BountyItem
import com.jason.alp_vp.data.service.BountyService
import com.jason.alp_vp.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BountyDetailViewModel : ViewModel() {

    private val bountyService = ApiClient.client.create(BountyService::class.java)

    private val _bountyDetail = MutableStateFlow<BountyItem?>(null)
    val bountyDetail = _bountyDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _claimSuccess = MutableStateFlow(false)
    val claimSuccess = _claimSuccess.asStateFlow()

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

    fun clearError() {
        _error.value = null
    }

    fun resetClaimSuccess() {
        _claimSuccess.value = false
    }
}

