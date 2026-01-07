package com.jason.alp_vp.utils

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREF_NAME = "auth_prefs"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USERNAME = "username"
    private const val KEY_EMAIL = "email"
    private const val KEY_ROLE = "role"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String?) {
        if (token.isNullOrBlank()) {
            throw IllegalArgumentException("Token cannot be null or empty")
        }
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun saveUserData(id: Int, username: String?, email: String, role: String? = null) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, id)
            putString(KEY_USERNAME, username ?: email.substringBefore("@"))  // Use email prefix if username is null
            putString(KEY_EMAIL, email)
            putString(KEY_ROLE, role ?: "USER")  // Default to "USER" if role is null
            apply()
        }
    }

    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun getUsername(): String? {
        return prefs.getString(KEY_USERNAME, null)
    }

    fun getEmail(): String? {
        return prefs.getString(KEY_EMAIL, null)
    }

    fun getRole(): String? {
        return prefs.getString(KEY_ROLE, null)
    }

    fun getUserData(): UserData? {
        val id = getUserId()
        if (id == -1) return null
        return UserData(
            id = id,
            username = getUsername(),
            email = getEmail() ?: "",
            role = getRole() ?: "user"
        )
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    fun clearToken() {
        prefs.edit().clear().apply()
    }
}

data class UserData(
    val id: Int,
    val username: String?,
    val email: String,
    val role: String
)

