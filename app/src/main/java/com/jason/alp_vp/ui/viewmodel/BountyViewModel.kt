package com.jason.alp_vp.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.api.Result
import com.jason.alp_vp.data.repository.BountyRepository
import com.jason.alp_vp.ui.model.Bounty
import com.jason.alp_vp.ui.model.EventPost
import kotlinx.coroutines.launch

class BountyViewModel(
    private val bountyRepository: BountyRepository = BountyRepository.getInstance()
) : ViewModel() {

    var activeBounties by mutableStateOf(listOf<Bounty>())
        private set

    var events by mutableStateOf(listOf<EventPost>())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadBounties()
        loadDummyEvents()
    }

    /**
     * Load all bounties from repository
     */
    fun loadBounties() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            when (val result = bountyRepository.getAllBounties()) {
                is Result.Success -> {
                    activeBounties = result.data
                }
                is Result.Error -> {
                    errorMessage = result.message ?: "Failed to load bounties"
                    // Optionally keep the old data or clear it
                }
                is Result.Loading -> {
                    // Already set isLoading = true
                }
            }

            isLoading = false
        }
    }

    /**
     * Search bounties
     */
    fun searchBounties(query: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            when (val result = bountyRepository.searchBounties(query)) {
                is Result.Success -> {
                    activeBounties = result.data
                }
                is Result.Error -> {
                    errorMessage = result.message ?: "Failed to search bounties"
                }
                is Result.Loading -> {
                    // Loading
                }
            }

            isLoading = false
        }
    }

    /**
     * Get bounty by ID
     */
    suspend fun getBountyById(bountyId: String): Bounty? {
        return when (val result = bountyRepository.getBountyById(bountyId)) {
            is Result.Success -> result.data
            is Result.Error -> {
                errorMessage = result.message ?: "Failed to get bounty"
                null
            }
            is Result.Loading -> null
        }
    }

    /**
     * Submit application for a bounty
     */
    fun submitApplication(
        bountyId: String,
        portfolioLinks: List<String>,
        cvImageUrl: String,
        whyHireYou: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            when (val result = bountyRepository.submitApplication(
                bountyId, portfolioLinks, cvImageUrl, whyHireYou
            )) {
                is Result.Success -> {
                    onSuccess()
                    // Remove from active bounties if needed
                    activeBounties = activeBounties.filterNot { it.id == bountyId }
                }
                is Result.Error -> {
                    val error = result.message ?: "Failed to submit application"
                    errorMessage = error
                    onError(error)
                }
                is Result.Loading -> {
                    // Loading
                }
            }

            isLoading = false
        }
    }

    private fun loadDummyEvents() {
        events = listOf(
            EventPost(
                id = "1",
                title = "AI Seminar",
                organizer = "Tech Institute",
                description = "Seminar about AI Trends",
                registered = 45,
                capacity = 100,
                badgeEmoji = "🤖"
            ),
            EventPost(
                id = "2",
                title = "Startup Bootcamp",
                organizer = "Startup Hub",
                description = "Learn about startups",
                registered = 30,
                capacity = 50,
                badgeEmoji = "🚀"
            )
        )
    }

    fun submitBounty(id: String) {
        activeBounties = activeBounties.filterNot { it.id == id }
    }

    fun canTakeMore(): Boolean = activeBounties.size < 3
}


