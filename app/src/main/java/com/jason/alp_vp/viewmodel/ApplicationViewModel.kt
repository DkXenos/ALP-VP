package com.jason.alp_vp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.model.Application
import com.jason.alp_vp.model.ApplicationStatus
import com.jason.alp_vp.repository.MockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ApplicationViewModel(private val repository: MockRepository = MockRepository()) : ViewModel() {

    private val _applicationState = MutableStateFlow<ApplicationState>(ApplicationState.Idle)
    val applicationState: StateFlow<ApplicationState> = _applicationState

    private val _applications = MutableStateFlow<List<Application>>(emptyList())
    val applications: StateFlow<List<Application>> = _applications

    fun submitApplication(bountyId: String, coverLetter: String) {
        viewModelScope.launch {
            _applicationState.value = ApplicationState.Loading
            val result = repository.submitApplication(bountyId, coverLetter)
            _applicationState.value = if (result.isSuccess) {
                ApplicationState.Success
            } else {
                ApplicationState.Error(result.exceptionOrNull()?.message ?: "Failed to submit")
            }
        }
    }

    fun loadApplicationsForBounty(bountyId: String) {
        viewModelScope.launch {
            _applications.value = repository.getApplicationsForBounty(bountyId)
        }
    }

    fun updateApplicationStatus(applicationId: String, status: ApplicationStatus) {
        viewModelScope.launch {
            repository.updateApplicationStatus(applicationId, status)
            // Refresh the list
            val currentList = _applications.value
            _applications.value = currentList.map {
                if (it.id == applicationId) it.copy(status = status) else it
            }
        }
    }

    fun resetState() {
        _applicationState.value = ApplicationState.Idle
    }
}

sealed class ApplicationState {
    object Idle : ApplicationState()
    object Loading : ApplicationState()
    object Success : ApplicationState()
    data class Error(val message: String) : ApplicationState()
}

