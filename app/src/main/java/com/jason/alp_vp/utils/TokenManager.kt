package com.jason.alp_vp.utils

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREF_NAME = "auth_prefs"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USERNAME = "username"
    private const val KEY_EMAIL = "email"

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

    fun saveUserData(id: Int, username: String?, email: String) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, id)
            putString(KEY_USERNAME, username ?: email.substringBefore("@"))  // Use email prefix if username is null
            putString(KEY_EMAIL, email)
            apply()
        }
    }

    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun getUsername(): String? {
        return prefs.getString(KEY_USERNAME, null)
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    fun clearToken() {
        prefs.edit().clear().apply()
    }
}

