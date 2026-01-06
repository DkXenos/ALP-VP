package com.jason.alp_vp.data.container

import com.jason.alp_vp.data.service.*
import com.jason.alp_vp.utils.TokenManager
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer {
    companion object {
        private const val BASE_URL = "http://10.0.2.16:57146/api/"
    }

    // OkHttp client with JWT token interceptor
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
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
                    return chain.proceed(request)
                }
            })
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

    val voteRepository: com.jason.alp_vp.data.repository.VoteRepository by lazy {
        com.jason.alp_vp.data.repository.VoteRepository(voteService)
    }

    val commentRepository: com.jason.alp_vp.data.repository.CommentRepository by lazy {
        com.jason.alp_vp.data.repository.CommentRepository(commentService)
    }

    val eventRepository: com.jason.alp_vp.data.repository.EventRepository by lazy {
        com.jason.alp_vp.data.repository.EventRepository(eventService)
    }

    val postRepository: com.jason.alp_vp.data.repository.PostRepository by lazy {
        com.jason.alp_vp.data.repository.PostRepository(postService, commentRepository)
    }

    val companyRepository: com.jason.alp_vp.data.repository.CompanyRepository by lazy {
        com.jason.alp_vp.data.repository.CompanyRepository(companyService)
    }
}

