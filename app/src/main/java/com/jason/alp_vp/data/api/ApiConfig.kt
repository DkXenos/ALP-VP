package com.jason.alp_vp.data.api

/**
 * API Configuration
 * Update BASE_URL when your backend is ready
 */
object ApiConfig {
    // TODO: Replace with your actual backend URL
    // For local development on emulator, use: http://10.0.2.2:3000/api/
    // For local development on physical device, use: http://YOUR_IP:3000/api/
    // For production, use: https://your-domain.com/api/
    const val BASE_URL = "http://localhost:3000/api/"

    // Timeout settings (in seconds)
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L

    // Enable logging for debugging
    const val ENABLE_LOGGING = true
}

