package com.jason.alp_vp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.TokenManager
import com.jason.alp_vp.data.api.Result
import com.jason.alp_vp.data.repository.AuthRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for authentication
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val tokenManager = TokenManager(application)
    private val authRepository = AuthRepository(tokenManager)

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoggedIn by mutableStateOf(authRepository.isLoggedIn())
        private set

    /**
     * Login user
     */
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            when (val result = authRepository.login(email, password)) {
                is Result.Success -> {
                    isLoggedIn = true
                    isLoading = false
                    onSuccess()
                }
                is Result.Error -> {
                    val error = result.message ?: "Login failed"
                    errorMessage = error
                    isLoading = false
                    onError(error)
                }
                is Result.Loading -> {
                    // Already set isLoading = true
                }
            }
        }
    }

    /**
     * Register new user
     */
    fun register(
        email: String,
        password: String,
        username: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            when (val result = authRepository.register(email, password, username)) {
                is Result.Success -> {
                    isLoggedIn = true
                    isLoading = false
                    onSuccess()
                }
                is Result.Error -> {
                    val error = result.message ?: "Registration failed"
                    errorMessage = error
                    isLoading = false
                    onError(error)
                }
                is Result.Loading -> {
                    // Already set isLoading = true
                }
            }
        }
    }

    /**
     * Logout user
     */
    fun logout(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            authRepository.logout()
            isLoggedIn = false
            onComplete()
        }
    }

    /**
     * Clear error message
     */
    fun clearError() {
        errorMessage = null
    }

    /**
     * Check login status
     */
    fun checkLoginStatus(): Boolean {
        isLoggedIn = authRepository.isLoggedIn()
        return isLoggedIn
    }
}

