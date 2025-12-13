package com.jason.alp_vp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.model.AuthRequest
import com.jason.alp_vp.model.AuthResponse
import com.jason.alp_vp.network.ApiClient
import com.jason.alp_vp.network.AuthApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val api = ApiClient.client.create(AuthApi::class.java)

    private val _authResponse = MutableStateFlow<AuthResponse?>(null)
    val authResponse = _authResponse.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = api.login(AuthRequest(email = email, password = password))
                _authResponse.value = response
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun register(username: String, email: String, password: String, role: String = "TALENT") {
        viewModelScope.launch {
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
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
