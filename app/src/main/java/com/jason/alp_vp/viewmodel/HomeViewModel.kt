package com.jason.alp_vp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.repository.BountyRepository
import com.jason.alp_vp.ui.model.Bounty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BountyUiState {
    object Loading : BountyUiState()
    data class Success(val bounties: List<Bounty>) : BountyUiState()
    data class Error(val message: String) : BountyUiState()
}

class HomeViewModel : ViewModel() {
    private val repository = BountyRepository()

    private val _uiState = MutableStateFlow<BountyUiState>(BountyUiState.Loading)
    val uiState: StateFlow<BountyUiState> = _uiState.asStateFlow()

    init {
        loadBounties()
    }

    fun loadBounties() {
        viewModelScope.launch {
            _uiState.value = BountyUiState.Loading
            repository.getBounties()
                .onSuccess { bounties ->
                    _uiState.value = BountyUiState.Success(bounties)
                }
                .onFailure { exception ->
                    _uiState.value = BountyUiState.Error(
                        exception.message ?: "Unknown error occurred"
                    )
                }
        }
    }

    fun retry() {
        loadBounties()
    }
}
