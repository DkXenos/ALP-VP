package com.jason.alp_vp.data.repository

import com.jason.alp_vp.data.TokenManager
import com.jason.alp_vp.data.api.ApiConfig
import com.jason.alp_vp.data.api.Result
import com.jason.alp_vp.model.AuthResponse
import com.jason.alp_vp.model.LoginRequest
import com.jason.alp_vp.model.RegisterRequest
import com.jason.alp_vp.network.AuthApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Repository for authentication operations
 */
class AuthRepository(
    private val tokenManager: TokenManager
) {

    private val authApi: AuthApi by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (ApiConfig.ENABLE_LOGGING) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(ApiConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(ApiConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    /**
     * Login user
     */
    suspend fun login(email: String, password: String): Result<AuthResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(email, password)
                val response = authApi.login(request)

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!

                    if (authResponse.success && authResponse.token != null && authResponse.user != null) {
                        // Save token and user info
                        tokenManager.saveToken(
                            token = authResponse.token,
                            userId = authResponse.user.id,
                            email = authResponse.user.email,
                            name = authResponse.user.name
                        )
                        Result.Success(authResponse)
                    } else {
                        Result.Error(
                            Exception(authResponse.error ?: "Login failed"),
                            authResponse.error
                        )
                    }
                } else {
                    Result.Error(
                        Exception("HTTP ${response.code()}: ${response.message()}"),
                        response.message()
                    )
                }
            } catch (e: Exception) {
                Result.Error(e, "Login failed: ${e.message}")
            }
        }

    /**
     * Register new user
     */
    suspend fun register(email: String, password: String, username: String): Result<AuthResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = RegisterRequest(username, email, password)
                val response = authApi.register(request)

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!

                    if (authResponse.success && authResponse.token != null && authResponse.user != null) {
                        // Save token and user info
                        tokenManager.saveToken(
                            token = authResponse.token,
                            userId = authResponse.user.id,
                            email = authResponse.user.email,
                            name = authResponse.user.name
                        )
                        Result.Success(authResponse)
                    } else {
                        Result.Error(
                            Exception(authResponse.error ?: "Registration failed"),
                            authResponse.error
                        )
                    }
                } else {
                    Result.Error(
                        Exception("HTTP ${response.code()}: ${response.message()}"),
                        response.message()
                    )
                }
            } catch (e: Exception) {
                Result.Error(e, "Registration failed: ${e.message}")
            }
        }

    /**
     * Logout user
     */
    suspend fun logout(): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val token = tokenManager.getAuthHeader()
            if (token != null) {
                // Optional: Call backend logout endpoint
                authApi.logout(token)
            }

            // Clear local token
            tokenManager.clearToken()
            Result.Success(true)
        } catch (e: Exception) {
            // Still clear local token even if backend call fails
            tokenManager.clearToken()
            Result.Success(true)
        }
    }

    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }

    /**
     * Get current user info from token
     */
    fun getCurrentUserInfo(): Triple<String?, String?, String?> {
        return Triple(
            tokenManager.getUserId(),
            tokenManager.getUserEmail(),
            tokenManager.getUserName()
        )
    }
}

