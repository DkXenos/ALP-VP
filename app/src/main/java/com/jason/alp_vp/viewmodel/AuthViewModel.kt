package com.jason.alp_vp.viewmodel

import android.util.Log
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

    fun login(email: String, password: String, isCompany: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                if (isCompany) {
                    // Company login - data wrapped in "data" object
                    Log.d("AuthViewModel", "Attempting company login for: $email")
                    val companyResponse = api.companyLogin(LoginRequest(email = email, password = password))

                    // Debug logging
                    Log.d("AuthViewModel", "Company response received")
                    Log.d("AuthViewModel", "Data: ${companyResponse.data}")
                    Log.d("AuthViewModel", "Token: ${companyResponse.data.token}")
                    Log.d("AuthViewModel", "Company: ${companyResponse.data.company}")
                    Log.d("AuthViewModel", "Company name: ${companyResponse.data.company?.name}")

                    // Validate response
                    if (companyResponse.data.token.isNullOrBlank()) {
                        Log.e("AuthViewModel", "Token is null or blank!")
                        throw Exception("Invalid response: token is missing")
                    }
                    if (companyResponse.data.company == null) {
                        Log.e("AuthViewModel", "Company data is null!")
                        throw Exception("Invalid response: company data is missing")
                    }

                    Log.d("AuthViewModel", "Validation passed, saving token and company data")

                    // Save token and company data
                    TokenManager.saveToken(companyResponse.data.token)
                    TokenManager.saveUserData(
                        id = companyResponse.data.company.id,
                        username = companyResponse.data.company.name,  // Use company name as username
                        email = companyResponse.data.company.email,
                        role = "COMPANY"  // Set role as COMPANY
                    )

                    Log.d("AuthViewModel", "Company login successful!")

                    // Create compatible AuthResponse for state
                    _authResponse.value = com.jason.alp_vp.model.AuthResponse(
                        data = com.jason.alp_vp.model.UserData(
                            id = companyResponse.data.company.id,
                            username = companyResponse.data.company.name,
                            email = companyResponse.data.company.email,
                            role = "COMPANY",
                            token = companyResponse.data.token
                        )
                    )
                } else {
                    // User login - standard response structure
                    val response = api.login(LoginRequest(email = email, password = password))
                    _authResponse.value = response

                    // Save token and user data
                    TokenManager.saveToken(response.data.token)
                    TokenManager.saveUserData(
                        id = response.data.id,
                        username = response.data.username ?: response.data.email.substringBefore("@"),
                        email = response.data.email,
                        role = response.data.role
                    )
                }

                _isLoggedIn.value = true
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login failed", e)
                Log.e("AuthViewModel", "Error message: ${e.message}")
                Log.e("AuthViewModel", "Error cause: ${e.cause}")
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
                    RegisterRequest(
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
                    username = response.data.username ?: response.data.email.substringBefore("@"),  // Use email prefix if username is null
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
