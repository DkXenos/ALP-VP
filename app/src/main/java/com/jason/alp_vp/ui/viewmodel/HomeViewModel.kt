package com.jason.alp_vp.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.jason.alp_vp.data.DummyDataRepository
import com.jason.alp_vp.ui.model.Bounty

class HomeViewModel : ViewModel() {

    var bounties by mutableStateOf(listOf<Bounty>())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadBounties()
    }

    /**
     * Load bounties from repository.
     * TODO: Replace with API call when backend is ready.
     */
    fun loadBounties() {
        isLoading = true
        // Simulate loading
        bounties = DummyDataRepository.getAllBounties()
        isLoading = false
    }

    /**
     * Search bounties by query.
     * TODO: Replace with API call when backend is ready.
     */
    fun searchBounties(query: String): List<Bounty> {
        return DummyDataRepository.searchBounties(query)
    }

    /**
     * Filter bounties by type.
     * TODO: Replace with API call when backend is ready.
     */
    fun filterByType(type: String): List<Bounty> {
        return DummyDataRepository.filterBountiesByType(type)
    }
}

