package com.jason.alp_vp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.model.AuthRequest
import com.jason.alp_vp.model.AuthResponse
import com.jason.alp_vp.network.ApiClient
import com.jason.alp_vp.network.AuthApi
import com.jason.alp_vp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val api = ApiClient.client.create(AuthApi::class.java)

    private val _authResponse = MutableStateFlow<AuthResponse?>(null)
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

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = api.login(AuthRequest(email = email, password = password))
                _authResponse.value = response

                // Save token and user data
                TokenManager.saveToken(response.data.token)
                TokenManager.saveUserData(
                    id = response.data.id,
                    username = response.data.username,
                    email = response.data.email,
                    role = response.data.role
                )
                _isLoggedIn.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Login failed"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(username: String, email: String, password: String, role: String = "TALENT") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = api.register(
                    AuthRequest(
                        username = username,
                        email = email,
                        password = password,
                        role = role
                    )
                )
                _authResponse.value = response

                // Save token and user data
                TokenManager.saveToken(response.data.token)
                TokenManager.saveUserData(
                    id = response.data.id,
                    username = response.data.username,
                    email = response.data.email,
                    role = response.data.role
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
        TokenManager.clearToken()
        _isLoggedIn.value = false
        _authResponse.value = null
    }

    fun checkLoginStatus() {
        _isLoggedIn.value = TokenManager.isLoggedIn()
    }
}
