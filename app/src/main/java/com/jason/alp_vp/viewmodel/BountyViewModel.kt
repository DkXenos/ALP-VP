package com.jason.alp_vp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.model.Bounty
import com.jason.alp_vp.repository.MockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BountyViewModel : ViewModel() {

    private val _bounties = MutableStateFlow<List<Bounty>>(emptyList())
    val bounties: StateFlow<List<Bounty>> = _bounties

    private val _selectedBounty = MutableStateFlow<Bounty?>(null)
    val selectedBounty: StateFlow<Bounty?> = _selectedBounty

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter

    init {
        loadBounties()
    }

    private fun loadBounties() {
        viewModelScope.launch {
            MockRepository.bounties.collect { bountyList ->
                _bounties.value = bountyList
            }
        }
    }

    fun searchBounties(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) {
            applyFilter(_selectedFilter.value)
        } else {
            _bounties.value = MockRepository.searchBounties(query)
        }
    }

    fun applyFilter(category: String) {
        _selectedFilter.value = category
        _bounties.value = MockRepository.filterBounties(category)
    }

    fun selectBounty(bountyId: String) {
        _selectedBounty.value = MockRepository.getBountyById(bountyId)
    }

    fun clearSelection() {
        _selectedBounty.value = null
    }
}

