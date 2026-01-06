package com.jason.alp_vp.data.container

import com.jason.alp_vp.data.repository.EventRepository
import com.jason.alp_vp.data.repository.PostRepository
import com.jason.alp_vp.data.service.*
import com.jason.alp_vp.utils.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Application Container - Single Source of Truth for Network & Dependency Injection
 *
 * This container manages:
 * - Retrofit configuration with JWT authentication
 * - Service instantiation (lazy initialization)
 * - Network logging and timeouts
 *
 * Usage in ViewModel:
 * ```
 * class MyViewModel(
 *     private val container: AppContainer = AppContainer()
 * ) : ViewModel() {
 *     private val service = container.myService
 * }
 * ```
 */
class AppContainer {
    companion object {
        private const val BASE_URL = "http://192.168.30.108:3000/api/"
    }

    // Logging interceptor for debugging API calls
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttp client with JWT token interceptor
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()

                // Add JWT token to all requests if available
                try {
                    val token = TokenManager.getToken()
                    if (token != null) {
                        requestBuilder.header("Authorization", "Bearer $token")
                    }
                } catch (e: UninitializedPropertyAccessException) {
                    // TokenManager not initialized yet, continue without token
                } catch (e: Exception) {
                    // Any other error, continue without token
                }

                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit instance (lazy initialization)
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ===== Services (Lazy Initialization) =====

    val profileService: ProfileService by lazy {
        retrofit.create(ProfileService::class.java)
    }

    val bountyService: BountyService by lazy {
        retrofit.create(BountyService::class.java)
    }

    val authService: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val postService: PostService by lazy {
        retrofit.create(PostService::class.java)
    }

    val commentService: CommentService by lazy {
        retrofit.create(CommentService::class.java)
    }

    val eventService: EventService by lazy {
        retrofit.create(EventService::class.java)
    }

    val voteService: VoteService by lazy {
        retrofit.create(VoteService::class.java)
    }

    val companyService: CompanyService by lazy {
        retrofit.create(CompanyService::class.java)
    }

    // ===== Repositories (Lazy Initialization) =====

    val eventRepository: EventRepository by lazy {
        EventRepository(eventService)
    }

    val postRepository: PostRepository by lazy {
        PostRepository(postService, commentRepository)
    }

    val voteRepository: com.jason.alp_vp.data.repository.VoteRepository by lazy {
        com.jason.alp_vp.data.repository.VoteRepository(voteService)
    }

    val commentRepository: com.jason.alp_vp.data.repository.CommentRepository by lazy {
        com.jason.alp_vp.data.repository.CommentRepository(commentService)
    }
}

