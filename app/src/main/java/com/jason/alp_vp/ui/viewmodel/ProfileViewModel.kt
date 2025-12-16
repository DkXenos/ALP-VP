package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.service.*
import com.jason.alp_vp.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val profileService = ApiClient.client.create(ProfileService::class.java)

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
                val response = profileService.getProfile()

                if (response.isSuccessful && response.body() != null) {
                    val profileData = response.body()!!.data
                    _profileData.value = profileData
                    Log.d("ProfileViewModel", "Profile loaded successfully")
                    Log.d("ProfileViewModel", "Profile ID: ${profileData.id}")
                    Log.d("ProfileViewModel", "Profile username: ${profileData.username}")
                    Log.d("ProfileViewModel", "Profile name: ${profileData.name}")
                    Log.d("ProfileViewModel", "Profile email: ${profileData.email}")
                    Log.d("ProfileViewModel", "Profile role: ${profileData.role}")
                    Log.d("ProfileViewModel", "Display name: ${profileData.getDisplayName()}")
                } else {
                    val errorMsg = "Failed to load profile: ${response.code()} - ${response.message()}"
                    Log.e("ProfileViewModel", errorMsg)
                    _error.value = errorMsg
                    _error.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error loading profile: ${e.message}"
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
                val response = profileService.getProfileStats()

                if (response.isSuccessful && response.body() != null) {
                    _profileStats.value = response.body()!!.data
                    Log.d("ProfileViewModel", "Stats loaded: ${response.body()!!.data}")
                } else {
                    Log.e("ProfileViewModel", "Failed to load stats: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading stats: ${e.message}", e)
            }
        }
    }

    fun updateProfile(username: String, email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("ProfileViewModel", "Updating profile...")
                val response = profileService.updateProfile(
                    UpdateProfileRequest(username, email)
                )

                if (response.isSuccessful && response.body() != null) {
                    _profileData.value = response.body()!!.data
                    Log.d("ProfileViewModel", "Profile updated successfully")
                } else {
                    val errorMsg = "Failed to update profile: ${response.code()}"
                    Log.e("ProfileViewModel", errorMsg)
                    _error.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error updating profile: ${e.message}"
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

