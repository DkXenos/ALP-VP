package com.jason.alp_vp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.model.LoginRequest
import com.jason.alp_vp.model.RegisterRequest
import com.jason.alp_vp.network.ApiClient
import com.jason.alp_vp.network.AuthApi
import com.jason.alp_vp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val api = ApiClient.client.create(AuthApi::class.java)

    private val _authResponse = MutableStateFlow<com.jason.alp_vp.model.AuthResponse?>(null)
    val authResponse = _authResponse.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        checkLoginStatus()
    }

    fun login(email: String, password: String, accountType: String = "USER") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Map USER to TALENT for backend compatibility
                val roleForBackend = if (accountType == "USER") "TALENT" else "COMPANY"

                val response = api.login(LoginRequest(email = email, password = password))
                _authResponse.value = response

                // Validate that the user's role matches the selected account type
                val userRole = response.data.role.uppercase()
                if (userRole != roleForBackend) {
                    _error.value = "Invalid credentials for the selected account type. Please check your selection."
                    _isLoading.value = false
                    return@launch
                }

                // Save token and user data with account type (USER or COMPANY)
                TokenManager.saveToken(response.data.token)
                TokenManager.saveUserData(
                    id = response.data.id,
                    username = response.data.username,
                    email = response.data.email,
                    accountType = accountType // Save as "USER" or "COMPANY"
                )
                _isLoggedIn.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Login failed"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(username: String, email: String, password: String, accountType: String = "USER") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Map USER to TALENT for backend compatibility
                val roleForBackend = if (accountType == "USER") "TALENT" else "COMPANY"

                val response = api.register(
                    RegisterRequest(
                        username = username,
                        email = email,
                        password = password,
                        role = roleForBackend
                    )
                )
                _authResponse.value = response

                // Save token and user data with account type (USER or COMPANY)
                TokenManager.saveToken(response.data.token)
                TokenManager.saveUserData(
                    id = response.data.id,
                    username = response.data.username,
                    email = response.data.email,
                    accountType = accountType // Save as "USER" or "COMPANY"
                )
                _isLoggedIn.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Registration failed"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        try {
            TokenManager.clearToken()
        } catch (e: Exception) {
            // Ignore errors during logout
        }
        _isLoggedIn.value = false
        _authResponse.value = null
    }

    fun checkLoginStatus() {
        try {
            _isLoggedIn.value = TokenManager.isLoggedIn()
        } catch (e: UninitializedPropertyAccessException) {
            // TokenManager not initialized yet, default to false
            _isLoggedIn.value = false
        } catch (e: Exception) {
            // Any other error, default to false
            _isLoggedIn.value = false
        }
    }
}
