package com.jason.alp_vp.data.container

import com.google.gson.GsonBuilder
import com.jason.alp_vp.data.repository.CommentRepository
import com.jason.alp_vp.data.repository.EventRepository
import com.jason.alp_vp.data.repository.PostRepository
import com.jason.alp_vp.data.repository.VoteRepository
import com.jason.alp_vp.data.service.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import java.util.concurrent.TimeUnit
import com.jason.alp_vp.utils.TokenManager

class AppContainer {

    companion object {
        // Ensure trailing slash — Retrofit requires baseUrl to end with '/'
        const val BASE_URL = "http://10.0.176.184:3000/api/"
    }

    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .create()

    private val okHttpClient: OkHttpClient by lazy {
        val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            try {
                val token = TokenManager.getToken()
                if (!token.isNullOrEmpty()) {
                    requestBuilder.header("Authorization", "Bearer $token")
                }
            } catch (_: Exception) {
                // TokenManager may not be initialized yet; ignore and proceed without header
            }
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    // Service instances
    @Suppress("unused")
    val commentService: CommentService by lazy {
        retrofit.create(CommentService::class.java)
    }

    @Suppress("unused")
    val companyService: CompanyService by lazy {
        retrofit.create(CompanyService::class.java)
    }

    @Suppress("unused")
    val eventService: EventService by lazy {
        retrofit.create(EventService::class.java)
    }

    @Suppress("unused")
    val postService: PostService by lazy {
        retrofit.create(PostService::class.java)
    }

    @Suppress("unused")
    val voteService: VoteService by lazy {
        retrofit.create(VoteService::class.java)
    }

    // Repository instances
    val commentRepository: CommentRepository by lazy {
        CommentRepository(commentService)
    }

    val voteRepository: VoteRepository by lazy {
        VoteRepository(voteService)
    }

    val postRepository: PostRepository by lazy {
        PostRepository(postService, commentRepository)
    }

    val eventRepository: EventRepository by lazy {
        EventRepository(eventService)
    }
}