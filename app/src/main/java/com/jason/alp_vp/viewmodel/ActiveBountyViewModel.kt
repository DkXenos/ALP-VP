package com.jason.alp_vp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.model.Bounty
import com.jason.alp_vp.model.BountyStatus
import com.jason.alp_vp.repository.MockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ActiveBountyViewModel : ViewModel() {

    private val _activeBounties = MutableStateFlow<List<Bounty>>(emptyList())
    val activeBounties: StateFlow<List<Bounty>> = _activeBounties

    init {
        loadActiveBounties()
    }

    private fun loadActiveBounties() {
        viewModelScope.launch {
            // Get bounties that are in progress or submitted for current user
            _activeBounties.value = MockRepository.getActiveBountiesForUser()
        }
    }

    fun refreshActiveBounties() {
        loadActiveBounties()
    }
}

