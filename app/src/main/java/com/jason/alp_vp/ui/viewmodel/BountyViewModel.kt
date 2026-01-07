package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
import com.jason.alp_vp.data.service.toUiModel
import com.jason.alp_vp.ui.model.Bounty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BountyViewModel(
    private val container: AppContainer = AppContainer()
) : ViewModel() {

    private val bountyService = container.bountyService

    private val _bounties = MutableStateFlow(
        listOf(
            Bounty("1", "Build Website", "BlueTech", "31 Dec 2025", 300, 500000, "active"),
            Bounty("2", "UI Redesign", "Aura Corp", "15 Jan 2026", 200, 350000, "active"),
        )
    )
    val bounties: StateFlow<List<Bounty>> = _bounties

    fun markBountyDone(bountyId: String) {
        _bounties.value = _bounties.value.filterNot { it.id == bountyId }
    }

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
                val response = bountyService.getAllBounties()

                if (response.isSuccessful && response.body() != null) {
                    val bountiesResponse = response.body()!!
                    Log.d("BountyViewModel", "Loaded ${bountiesResponse.data.size} bounties from API")

                    // Debug log each bounty
                    bountiesResponse.data.forEachIndexed { index, item ->
                        Log.d("BountyViewModel", "Bounty $index: id=${item.id}, title=${item.title}, company=${item.company}")
                    }

                    // Filter and map with detailed error handling
                    val mappedBounties = bountiesResponse.data
                        .mapNotNull { item ->
                            try {
                                if (item.id.isBlank()) {
                                    Log.w("BountyViewModel", "Skipping bounty with blank ID: ${item.title}")
                                    null
                                } else {
                                    item.toUiModel()
                                }
                            } catch (e: Exception) {
                                Log.e("BountyViewModel", "Error mapping bounty ${item.id}: ${e.message}", e)
                                null
                            }
                        }

                    _bounties.value = mappedBounties
                    Log.d("BountyViewModel", "Successfully mapped ${mappedBounties.size} bounties")
                } else {
                    val errorMsg = "Failed to load bounties: ${response.code()} - ${response.message()}"
                    Log.e("BountyViewModel", errorMsg)
                    _error.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error loading bounties: ${e.message}"
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
                val response = bountyService.getMyBounties()

                if (response.isSuccessful && response.body() != null) {
                    val myBountiesResponse = response.body()!!
                    Log.d("BountyViewModel", "Loaded ${myBountiesResponse.data.size} claimed bounties from API")

                    // Debug log each bounty
                    myBountiesResponse.data.forEachIndexed { index, item ->
                        Log.d("BountyViewModel", "MyBounty $index: id=${item.bounty_id}, title=${item.title}, company=${item.company}")
                    }

                    // Filter and map with detailed error handling
                    val mappedBounties = myBountiesResponse.data
                        .mapNotNull { item ->
                            try {
                                if (item.bounty_id.isBlank()) {
                                    Log.w("BountyViewModel", "Skipping my bounty with blank ID: ${item.title}")
                                    null
                                } else {
                                    item.toUiModel()
                                }
                            } catch (e: Exception) {
                                Log.e("BountyViewModel", "Error mapping my bounty ${item.bounty_id}: ${e.message}", e)
                                null
                            }
                        }

                    _myBounties.value = mappedBounties
                    Log.d("BountyViewModel", "Successfully mapped ${mappedBounties.size} my bounties")
                } else {
                    val errorMsg = "Failed to load my bounties: ${response.code()}"
                    Log.e("BountyViewModel", errorMsg)
                    _error.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error loading my bounties: ${e.message}"
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
                val response = bountyService.claimBounty(bountyId)

                if (response.isSuccessful) {
                    Log.d("BountyViewModel", "Successfully claimed bounty $bountyId")
                    // Reload bounties to reflect the change
                    loadAllBounties()
                    loadMyBounties()
                } else {
                    val errorMsg = "Failed to claim bounty: ${response.code()}"
                    Log.e("BountyViewModel", errorMsg)
                    _error.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error claiming bounty: ${e.message}"
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
                val response = bountyService.unclaimBounty(bountyId)

                if (response.isSuccessful) {
                    Log.d("BountyViewModel", "Successfully unclaimed bounty $bountyId")
                    // Reload bounties to reflect the change
                    loadAllBounties()
                    loadMyBounties()
                } else {
                    val errorMsg = "Failed to unclaim bounty: ${response.code()}"
                    Log.e("BountyViewModel", errorMsg)
                    _error.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error unclaiming bounty: ${e.message}"
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
                val response = bountyService.getBountyById(bountyId)

                if (response.isSuccessful && response.body() != null) {
                    Log.d("BountyViewModel", "Loaded bounty: ${response.body()!!.data}")
                } else {
                    Log.e("BountyViewModel", "Failed to load bounty: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("BountyViewModel", "Error loading bounty: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}

