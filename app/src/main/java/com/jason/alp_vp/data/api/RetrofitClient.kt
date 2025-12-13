package com.jason.alp_vp.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit client builder
 * Creates and configures the Retrofit instance
 */
object RetrofitClient {

    private var retrofit: Retrofit? = null

    /**
     * Get configured Retrofit instance
     */
    fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    /**
     * Get configured OkHttpClient with logging and timeouts
     */
    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(ApiConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(ApiConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)

        // Add logging interceptor for debugging
        if (ApiConfig.ENABLE_LOGGING) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(loggingInterceptor)
        }

        // Add custom headers if needed (e.g., authentication)
        builder.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                // TODO: Add authentication token when implementing user auth
                // .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }

        return builder.build()
    }

    /**
     * Get API Service instance
     */
    fun getApiService(): ApiService {
        return getRetrofit().create(ApiService::class.java)
    }
}

