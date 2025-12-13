package com.jason.alp_vp.data.api

/**
 * A generic wrapper for API call results
 * Handles success, error, and loading states
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception, val message: String? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

/**
 * Extension function to convert Retrofit Response to Result
 */
fun <T> retrofit2.Response<T>.toResult(): Result<T> {
    return try {
        if (this.isSuccessful) {
            val body = this.body()
            if (body != null) {
                Result.Success(body)
            } else {
                Result.Error(Exception("Empty response body"))
            }
        } else {
            Result.Error(
                Exception("API Error: ${this.code()}"),
                this.message()
            )
        }
    } catch (e: Exception) {
        Result.Error(e, e.message)
    }
}

