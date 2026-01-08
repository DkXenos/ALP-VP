package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
import com.jason.alp_vp.ui.model.Event
import com.jason.alp_vp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val container: AppContainer = AppContainer()
) : ViewModel() {

    private val eventRepository = container.eventRepository

    private val _eventDetail = MutableStateFlow<Event?>(null)
    val eventDetail = _eventDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess = _registerSuccess.asStateFlow()

    fun loadEventDetail(eventId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("EventDetailViewModel", "Loading event detail: $eventId")
                val event = eventRepository.getEventById(eventId)
                _eventDetail.value = event
                Log.d("EventDetailViewModel", "Event loaded: $event")
            } catch (e: Exception) {
                val errorMsg = "Error loading event: ${e.message}"
                Log.e("EventDetailViewModel", errorMsg, e)
                _error.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun registerToEvent(eventId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val userId = TokenManager.getUserId()

                // Validate user ID
                if (userId <= 0) {
                    throw IllegalStateException("User not logged in. Please log in to register for events.")
                }

                Log.d("EventDetailViewModel", "Registering to event: $eventId, userId: $userId")
                eventRepository.registerToEvent(eventId, userId)
                Log.d("EventDetailViewModel", "Event registration successful")
                _registerSuccess.value = true
                // Reload event to get updated registration count
                loadEventDetail(eventId)
            } catch (e: IllegalStateException) {
                val errorMsg = e.message ?: "User not logged in"
                Log.e("EventDetailViewModel", errorMsg, e)
                _error.value = errorMsg
            } catch (e: Exception) {
                val errorMsg = "Error registering to event: ${e.message}"
                Log.e("EventDetailViewModel", errorMsg, e)
                _error.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetRegisterSuccess() {
        _registerSuccess.value = false
    }
}

