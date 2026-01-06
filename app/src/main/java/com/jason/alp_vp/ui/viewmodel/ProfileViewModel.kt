package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
import com.jason.alp_vp.data.service.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val container: AppContainer = AppContainer()
) : ViewModel() {

    // ✅ CORRECT: Use repository instead of service
    private val profileRepository = container.profileRepository

    private val _profileData = MutableStateFlow<ProfileData?>(null)
    val profileData = _profileData.asStateFlow()

    private val _profileStats = MutableStateFlow<ProfileStats?>(null)
    val profileStats = _profileStats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        loadProfile()
        loadProfileStats()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("ProfileViewModel", "Loading profile...")
                // ✅ Use repository - it handles error checking
                val response = profileRepository.getProfile()
                
                val profileData = response.data
                _profileData.value = profileData
                Log.d("ProfileViewModel", "Profile loaded successfully")
                Log.d("ProfileViewModel", "Profile ID: ${profileData.id}")
                Log.d("ProfileViewModel", "Profile username: ${profileData.username}")
                Log.d("ProfileViewModel", "Profile name: ${profileData.name}")
                Log.d("ProfileViewModel", "Profile email: ${profileData.email}")
                Log.d("ProfileViewModel", "Profile role: ${profileData.role}")
                Log.d("ProfileViewModel", "Profile XP: ${profileData.xp}")
                Log.d("ProfileViewModel", "Profile Balance: ${profileData.balance}")
                Log.d("ProfileViewModel", "Display name: ${profileData.getDisplayName()}")
            } catch (e: Exception) {
                // Repository provides user-friendly error messages
                val errorMsg = e.message ?: "Error loading profile"
                Log.e("ProfileViewModel", errorMsg, e)
                _error.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadProfileStats() {
        viewModelScope.launch {
            try {
                Log.d("ProfileViewModel", "Loading profile stats...")
                // ✅ Use repository
                val response = profileRepository.getProfileStats()
                _profileStats.value = response.data
                Log.d("ProfileViewModel", "Stats loaded: ${response.data}")
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Error loading stats"
                Log.e("ProfileViewModel", errorMsg, e)
            }
        }
    }

    fun updateProfile(username: String, email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("ProfileViewModel", "Updating profile...")
                // ✅ Use repository
                val response = profileRepository.updateProfile(username, email)
                _profileData.value = response.data
                Log.d("ProfileViewModel", "Profile updated successfully")
            } catch (e: IllegalArgumentException) {
                // Validation error
                val errorMsg = e.message ?: "Invalid input"
                Log.e("ProfileViewModel", errorMsg, e)
                _error.value = errorMsg
            } catch (e: Exception) {
                // Network error
                val errorMsg = e.message ?: "Error updating profile"
                Log.e("ProfileViewModel", errorMsg, e)
                _error.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        loadProfile()
        loadProfileStats()
    }
}

