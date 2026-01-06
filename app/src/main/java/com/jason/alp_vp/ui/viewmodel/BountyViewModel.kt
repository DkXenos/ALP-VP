package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
import com.jason.alp_vp.data.service.toUiModel
import com.jason.alp_vp.ui.model.Bounty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BountyViewModel(
    private val container: AppContainer = AppContainer()
) : ViewModel() {

<<<<<<< Updated upstream
=======
    // ✅ CORRECT: Use repository instead of service
>>>>>>> Stashed changes
    private val bountyRepository = container.bountyRepository

    private val _bounties = MutableStateFlow<List<Bounty>>(emptyList())
    val bounties = _bounties.asStateFlow()

    private val _myBounties = MutableStateFlow<List<Bounty>>(emptyList())
    val myBounties = _myBounties.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        loadAllBounties()
    }

    fun loadAllBounties() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("BountyViewModel", "Loading all bounties...")
<<<<<<< Updated upstream
                // ✅ Use repository - it handles error checking and validation
=======
                // ✅ Use repository
>>>>>>> Stashed changes
                val response = bountyRepository.getAllBounties()
                Log.d("BountyViewModel", "Loaded ${response.data.size} bounties")
                _bounties.value = response.data.map { it.toUiModel() }
            } catch (e: Exception) {
<<<<<<< Updated upstream
                // Repository provides user-friendly error messages
=======
>>>>>>> Stashed changes
                val errorMsg = e.message ?: "Error loading bounties"
                Log.e("BountyViewModel", errorMsg, e)
                _error.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMyBounties() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("BountyViewModel", "Loading my bounties...")
                // ✅ Use repository
                val response = bountyRepository.getMyClaimedBounties()
                Log.d("BountyViewModel", "Loaded ${response.data.size} claimed bounties")
                _myBounties.value = response.data.map { it.toUiModel() }
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Error loading my bounties"
                Log.e("BountyViewModel", errorMsg, e)
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
                Log.d("BountyViewModel", "Claiming bounty $bountyId...")
<<<<<<< Updated upstream
                // ✅ Use repository - it validates and provides user-friendly errors
=======
                // ✅ Use repository
>>>>>>> Stashed changes
                bountyRepository.claimBounty(bountyId)
                Log.d("BountyViewModel", "Successfully claimed bounty $bountyId")
                // Reload bounties to reflect the change
                loadAllBounties()
                loadMyBounties()
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Error claiming bounty"
                Log.e("BountyViewModel", errorMsg, e)
                _error.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun unclaimBounty(bountyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("BountyViewModel", "Unclaiming bounty $bountyId...")
<<<<<<< Updated upstream
                // ✅ Use repository - it validates and provides user-friendly errors
=======
                // ✅ Use repository
>>>>>>> Stashed changes
                bountyRepository.unclaimBounty(bountyId)
                Log.d("BountyViewModel", "Successfully unclaimed bounty $bountyId")
                // Reload bounties to reflect the change
                loadAllBounties()
                loadMyBounties()
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Error unclaiming bounty"
                Log.e("BountyViewModel", errorMsg, e)
                _error.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getBountyById(bountyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("BountyViewModel", "Loading bounty $bountyId...")
                // ✅ Use repository
                val response = bountyRepository.getBountyById(bountyId)
                Log.d("BountyViewModel", "Loaded bounty: ${response.data}")
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Error loading bounty"
                Log.e("BountyViewModel", errorMsg, e)
                _error.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }
}

