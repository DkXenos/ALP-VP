package com.jason.alp_vp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
import com.jason.alp_vp.model.LoginRequest
import com.jason.alp_vp.model.RegisterRequest
import com.jason.alp_vp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val container: AppContainer = AppContainer()
) : ViewModel() {

    private val api = container.authService

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
                    Log.d("AuthViewModel", "Attempting user login for: $email")
                    val response = api.login(LoginRequest(email = email, password = password))
                    _authResponse.value = response

                    Log.d("AuthViewModel", "User login successful!")
                    Log.d("AuthViewModel", "User ID: ${response.data.id}")
                    Log.d("AuthViewModel", "Username: ${response.data.username}")
                    Log.d("AuthViewModel", "Role from backend: ${response.data.role}")

                    // Save token and user data
                    TokenManager.saveToken(response.data.token)
                    TokenManager.saveUserData(
                        id = response.data.id,
                        username = response.data.username ?: response.data.email.substringBefore("@"),
                        email = response.data.email,
                        role = response.data.role  // Will default to "USER" if null
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

    fun register(username: String, email: String, password: String, role: String = "USER") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("AuthViewModel", "Registering with role: $role")

                if (role == "COMPANY") {
                    // Company registration - use company service
                    Log.d("AuthViewModel", "=== COMPANY REGISTRATION START ===")
                    Log.d("AuthViewModel", "Email: $email, Name: $username")

                    val companyResponse = container.companyService.register(
                        com.jason.alp_vp.data.service.CompanyRegisterRequest(
                            name = username,
                            email = email,
                            password = password,
                            description = null
                        )
                    )

                    Log.d("AuthViewModel", "Response code: ${companyResponse.code()}")
                    Log.d("AuthViewModel", "Response message: ${companyResponse.message()}")
                    Log.d("AuthViewModel", "Response headers: ${companyResponse.headers()}")

                    // Log raw response body for debugging
                    try {
                        val rawBody = companyResponse.raw().peekBody(Long.MAX_VALUE).string()
                        Log.d("AuthViewModel", "Raw response body: $rawBody")
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "Could not read raw body: ${e.message}")
                    }

                    if (!companyResponse.isSuccessful) {
                        val errorBody = companyResponse.errorBody()?.string()
                        Log.e("AuthViewModel", "Error body: $errorBody")
                        _error.value = "Registration failed: ${companyResponse.code()} - $errorBody"
                        throw Exception("Registration failed: ${companyResponse.code()}")
                    }

                    val body = companyResponse.body()
                    if (body == null) {
                        Log.e("AuthViewModel", "Response body is null!")
                        throw Exception("Company registration failed: Empty response")
                    }

                    Log.d("AuthViewModel", "✅ Company registration successful!")
                    Log.d("AuthViewModel", "Token: ${body.token.take(20)}...")
                    Log.d("AuthViewModel", "Company ID: ${body.company.id}")
                    Log.d("AuthViewModel", "Company name: ${body.company.name}")
                    Log.d("AuthViewModel", "Company email: ${body.company.email}")

                    // Save token and company data
                    TokenManager.saveToken(body.token)

                    // Handle String ID from backend - try to parse or use hash code
                    val companyId = body.company.id.toIntOrNull() ?: body.company.id.hashCode()
                    Log.d("AuthViewModel", "Parsed company ID: $companyId")

                    TokenManager.saveUserData(
                        id = companyId,
                        username = body.company.name,
                        email = body.company.email,
                        role = "COMPANY"
                    )

                    // Create compatible AuthResponse for state
                    _authResponse.value = com.jason.alp_vp.model.AuthResponse(
                        data = com.jason.alp_vp.model.UserData(
                            id = companyId,
                            username = body.company.name,
                            email = body.company.email,
                            role = "COMPANY",
                            token = body.token
                        )
                    )
                    _isLoggedIn.value = true
                    Log.d("AuthViewModel", "=== COMPANY REGISTRATION COMPLETE ===")
                } else {
                    // User registration - use standard auth endpoint with role "USER"
                    Log.d("AuthViewModel", "Attempting user registration for: $email")
                    val response = api.register(
                        RegisterRequest(
                            username = username,
                            email = email,
                            password = password,
                            role = "USER"  // Explicitly set to "USER"
                        )
                    )
                    _authResponse.value = response

                    // Save token and user data
                    TokenManager.saveToken(response.data.token)
                    TokenManager.saveUserData(
                        id = response.data.id,
                        username = response.data.username ?: response.data.email.substringBefore("@"),
                        email = response.data.email,
                        role = response.data.role
                    )
                    _isLoggedIn.value = true
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Registration failed", e)
                Log.e("AuthViewModel", "Exception type: ${e.javaClass.simpleName}")
                Log.e("AuthViewModel", "Exception message: ${e.message}")
                Log.e("AuthViewModel", "Exception cause: ${e.cause?.message}")

                // Extract more meaningful error message
                val errorMessage = when {
                    e.message?.contains("HTTP 400") == true -> {
                        "Invalid registration data. Please check your inputs."
                    }
                    e.message?.contains("HTTP 409") == true -> {
                        "Email already exists. Please use a different email."
                    }
                    e.message?.contains("HTTP 500") == true -> {
                        "Server error. Please try again later."
                    }
                    e.message?.contains("Unable to resolve host") == true -> {
                        "Cannot connect to server. Check your internet connection."
                    }
                    else -> e.message ?: "Registration failed. Please try again."
                }

                _error.value = errorMessage
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
