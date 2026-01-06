package com.jason.alp_vp.data.container

import com.jason.alp_vp.data.service.*
import com.jason.alp_vp.data.repository.ProfileRepository
import com.jason.alp_vp.data.repository.BountyRepository
import com.jason.alp_vp.data.repository.VoteRepository
import com.jason.alp_vp.data.repository.CommentRepository
import com.jason.alp_vp.data.repository.PostRepository
import com.jason.alp_vp.data.repository.EventRepository
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
 * - Service instantiation (lazy initialization - private)
 * - Repository instantiation (lazy initialization - public)
 * - Network logging and timeouts
 *
 * MVVM Architecture:
 * Backend API ← Service (private) ← Repository (public) ← ViewModel ← UI
 *
 * Usage in ViewModel:
 * ```
 * class ProfileViewModel(
 *     private val container: AppContainer = AppContainer()
 * ) : ViewModel() {
 *     private val profileRepository = container.profileRepository  // ✅ CORRECT
 *
 *     // ❌ WRONG: private val profileService = container.profileService
 *     // Never access services directly - use repositories!
 * }
 * ```
 */
class AppContainer {
    companion object {
        private const val BASE_URL = "http://10.0.177.118:3000/api/"
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

    // ===== Services (Lazy Initialization - Private) =====
    // Services should NOT be accessed directly by ViewModels.
    // Always use the Repository layer for proper separation of concerns.

    private val profileService: ProfileService by lazy {
        retrofit.create(ProfileService::class.java)
    }

    private val bountyService: BountyService by lazy {
        retrofit.create(BountyService::class.java)
    }

    private val authService: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    private val postService: PostService by lazy {
        retrofit.create(PostService::class.java)
    }

    private val commentService: CommentService by lazy {
        retrofit.create(CommentService::class.java)
    }

    private val eventService: EventService by lazy {
        retrofit.create(EventService::class.java)
    }

    private val voteService: VoteService by lazy {
        retrofit.create(VoteService::class.java)
    }

    private val companyService: CompanyService by lazy {
        retrofit.create(CompanyService::class.java)
    }

    // ===== Repositories (Lazy Initialization - Public) =====
    // ViewModels should ONLY access these repositories.
    // Repositories handle business logic, error handling, and data transformation.

    /**
     * ProfileRepository - Manages user profile data
     * Used in: ProfileViewModel, HomeViewModel
     */
<<<<<<< Updated upstream
    val profileRepository: com.jason.alp_vp.data.repository.ProfileRepository by lazy {
        com.jason.alp_vp.data.repository.ProfileRepository(profileService)
    }

    /**
     * BountyRepository - Manages bounty operations
     * Used in: BountyListViewModel, BountyDetailViewModel, ProfileViewModel
     */
    val bountyRepository: com.jason.alp_vp.data.repository.BountyRepository by lazy {
        com.jason.alp_vp.data.repository.BountyRepository(bountyService)
    }

    /**
     * VoteRepository - Manages voting on comments
     * Used in: CommentViewModel, PostDetailViewModel
     */
    val voteRepository: com.jason.alp_vp.data.repository.VoteRepository by lazy {
        com.jason.alp_vp.data.repository.VoteRepository(voteService)
    }

    /**
     * CommentRepository - Manages comments on posts
     * Used in: CommentViewModel, PostDetailViewModel, HomeViewModel
     */
    val commentRepository: com.jason.alp_vp.data.repository.CommentRepository by lazy {
        com.jason.alp_vp.data.repository.CommentRepository(commentService)
    }

    // TODO: Add remaining repositories as they are created:
    // - AuthRepository (for login, register, logout)
    // - PostRepository (for creating, updating, deleting posts)
    // - EventRepository (for event registration and management)
=======
    val profileRepository: ProfileRepository by lazy {
        ProfileRepository(profileService)
    }

    /**
     * BountyRepository - Manages bounty operations
     * Used in: BountyListViewModel, BountyDetailViewModel, ProfileViewModel
     */
    val bountyRepository: BountyRepository by lazy {
        BountyRepository(bountyService)
    }

    /**
     * EventRepository - Manages events
     * Used in: ForumPageViewModel, EventViewModels
     */
    val eventRepository: EventRepository by lazy {
        EventRepository(eventService)
    }

    /**
     * VoteRepository - Manages voting on comments
     * Used in: CommentViewModel, PostDetailViewModel
     */
    val voteRepository: VoteRepository by lazy {
        VoteRepository(voteService)
    }

    /**
     * CommentRepository - Manages comments on posts
     * Used in: CommentViewModel, PostDetailViewModel, HomeViewModel
     */
    val commentRepository: CommentRepository by lazy {
        CommentRepository(commentService)
    }

    /**
     * PostRepository - Manages posts and maps DTO -> UI model (needs CommentRepository)
     * Used in: ForumPageViewModel, HomeViewModel, PostDetailViewModel
     */
    val postRepository: PostRepository by lazy {
        PostRepository(postService, commentRepository)
    }

    // TODO: Add remaining repositories as they are created:
    // - AuthRepository (for login, register, logout)
>>>>>>> Stashed changes
    // - CompanyRepository (for company-specific operations)
}
