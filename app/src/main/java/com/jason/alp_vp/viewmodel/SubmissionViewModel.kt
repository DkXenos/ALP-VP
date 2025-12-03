package com.jason.alp_vp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.repository.MockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubmissionViewModel(private val repository: MockRepository = MockRepository()) : ViewModel() {

    private val _submissionState = MutableStateFlow<SubmissionState>(SubmissionState.Idle)
    val submissionState: StateFlow<SubmissionState> = _submissionState

    fun submitWork(bountyId: String, notes: String) {
        viewModelScope.launch {
            _submissionState.value = SubmissionState.Loading
            val result = repository.submitWork(bountyId, notes)
            _submissionState.value = if (result.isSuccess) {
                SubmissionState.Success
            } else {
                SubmissionState.Error(result.exceptionOrNull()?.message ?: "Submission failed")
            }
        }
    }

    fun resetState() {
        _submissionState.value = SubmissionState.Idle
    }
}

sealed class SubmissionState {
    object Idle : SubmissionState()
    object Loading : SubmissionState()
    object Success : SubmissionState()
    data class Error(val message: String) : SubmissionState()
}

