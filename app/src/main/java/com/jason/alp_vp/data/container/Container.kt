package com.jason.alp_vp.data.container
import com.jason.alp_vp.data.repository.UserRepository
import com.jason.alp_vp.data.repository.CommentRepository
import com.jason.alp_vp.data.repository.EventRepository
import com.jason.alp_vp.data.repository.PostRepository
import com.jason.alp_vp.data.repository.CompanyRepository
import com.jason.alp_vp.data.repository.VoteRepository
import com.jason.alp_vp.data.service.*
import com.jason.alp_vp.utils.TokenManager
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer {
    companion object {
        // Use localhost for reliable connection (run: adb reverse tcp:3000 tcp:3000)
        private const val BASE_URL = "http://10.229.135.2:3000/api/"
    }

    // OkHttp client with JWT token interceptor and logging
    private val okHttpClient: OkHttpClient by lazy {
        // Add logging interceptor
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log request and response bodies
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Add logging first
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
                    } catch (_: UninitializedPropertyAccessException) {
                        // TokenManager not initialized yet, continue without token
                    } catch (_: Exception) {
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

    val commentRepository: CommentRepository by lazy {
        CommentRepository(commentService)
    }

    val eventRepository: EventRepository by lazy {
        EventRepository(eventService)
    }

    val postRepository: PostRepository by lazy {
        PostRepository(postService, commentRepository)
    }

    val voteRepository: VoteRepository by lazy {
        VoteRepository(voteService)
    }

    val companyRepository: CompanyRepository by lazy {
        CompanyRepository(companyService)
    }
}

