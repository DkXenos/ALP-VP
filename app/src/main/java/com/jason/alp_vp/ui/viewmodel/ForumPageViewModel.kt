package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
import com.jason.alp_vp.ui.model.Event
import com.jason.alp_vp.ui.model.Post
import com.jason.alp_vp.ui.model.Comment
import com.jason.alp_vp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant

class ForumPageViewModel(
    private val container: AppContainer = AppContainer()
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    data class PostUi(
        val post: Post,
        val upvoteCount: Int,
        val downvoteCount: Int
    )

    private val _postUis = MutableStateFlow<List<PostUi>>(emptyList())
    val postUis: StateFlow<List<PostUi>> = _postUis

    // State untuk Post Detail
    private val _selectedPost = MutableStateFlow<Post?>(null)
    val selectedPost: StateFlow<Post?> = _selectedPost

    private val _selectedPostReplies = MutableStateFlow<List<Comment>>(emptyList())
    val selectedPostReplies: StateFlow<List<Comment>> = _selectedPostReplies

    // Expose a pre-formatted "time ago" string for the selected post so UI doesn't compute it
    private val _selectedPostTimeAgo = MutableStateFlow("")
    val selectedPostTimeAgo: StateFlow<String> = _selectedPostTimeAgo

    // Add missing essential states for production stability
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    // Track registered events
    private val _registeredEvents = MutableStateFlow<Set<Int>>(emptySet())
    val registeredEvents: StateFlow<Set<Int>> = _registeredEvents

    private val currentUserId: Int
        get() = try {
            val userId = TokenManager.getUserId()
            if (userId > 0) userId else 1 // Use 1 as fallback if invalid ID
        } catch (e: Exception) {
            Log.e("ForumPageViewModel", "Error getting user ID", e)
            1 // Fallback user ID
        }

    init {
        Log.d("ForumPageViewModel", "=== INITIALIZING FORUM PAGE VIEW MODEL ===")
        loadData()
    }

    private fun loadData() {
        Log.d("ForumPageViewModel", "=== LOAD DATA CALLED ===")
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            try {
                Log.d("ForumPageViewModel", "Starting to load events and posts...")

                // Load events from backend via repository
                val eventsList = try {
                    Log.d("ForumPageViewModel", "Calling eventRepository.getAllEvents()...")
                    val events = container.eventRepository.getAllEvents()
                    Log.d("ForumPageViewModel", "✅ Loaded ${events.size} events successfully")
                    events
                } catch (e: Exception) {
                    Log.e("ForumPageViewModel", "❌ Failed to load events: ${e.message}", e)
                    emptyList<Event>()
                }

                // Load posts from backend via repository
                val postsList = try {
                    Log.d("ForumPageViewModel", "Calling postRepository.getAllPosts()...")
                    val posts = container.postRepository.getAllPosts()
                    Log.d("ForumPageViewModel", "✅ Loaded ${posts.size} posts successfully")
                    posts.forEachIndexed { index, post ->
                        Log.d("ForumPageViewModel", "  Post $index: id=${post.id}, user=${post.authorName}, content=${post.content.take(50)}...")
                    }
                    posts
                } catch (e: Exception) {
                    Log.e("ForumPageViewModel", "❌ Failed to load posts: ${e.message}", e)
                    emptyList<Post>()
                }

                Log.d("ForumPageViewModel", "Setting events and posts to state...")
                _events.value = eventsList
                _posts.value = postsList
                recomputePostUis(postsList)
                Log.d("ForumPageViewModel", "✅ State updated - events: ${eventsList.size}, posts: ${postsList.size}, postUis: ${_postUis.value.size}")

            } catch (e: Exception) {
                Log.e("ForumPageViewModel", "❌ Unexpected error while loading data", e)
                _errorState.value = "Failed to load data: ${e.message}"
                // Set safe default values
                _events.value = emptyList()
                _posts.value = emptyList()
                _postUis.value = emptyList()
            } finally {
                _isLoading.value = false
                Log.d("ForumPageViewModel", "=== LOAD DATA COMPLETED ===")
            }
        }
    }

    // Add utility methods for production stability
    fun refreshData() {
        Log.d("ForumPageViewModel", "=== REFRESH DATA CALLED MANUALLY ===")
        loadData()
    }

    fun clearError() {
        _errorState.value = null
    }

    private fun recomputePostUis(posts: List<Post>) {
        val ui = posts.map { post ->
            var upvoteCount = 0
            var downvoteCount = 0

            post.comments.forEach { comment ->
                comment.commentVotes.forEach { vote ->
                    when (vote.voteType) {
                        "upvote" -> upvoteCount++
                        "downvote" -> downvoteCount++
                    }
                }
            }

            PostUi(post = post, upvoteCount = upvoteCount, downvoteCount = downvoteCount)
        }
        _postUis.value = ui
    }

    fun upvote(postId: Int) {
        // Post voting not supported - disabled
        Log.d("ForumPageVM", "Post voting not supported by backend")
    }

    fun downvote(postId: Int) {
        // Post voting not supported - disabled
        Log.d("ForumPageVM", "Post voting not supported by backend")
    }

    // Comment voting functions
    fun upvoteReply(commentId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("ForumPageVM", "Upvoting comment: $commentId")
                val success = container.voteRepository.addUpvote(commentId, currentUserId)
                if (success) {
                    Log.d("ForumPageVM", "Comment upvoted successfully")
                    // Reload the selected post to refresh comment votes
                    _selectedPost.value?.let { post ->
                        selectPost(post.id)
                    }
                    loadData()
                } else {
                    _errorState.value = "Failed to upvote comment"
                }
            } catch (e: Exception) {
                Log.e("ForumPageVM", "Error upvoting comment", e)
                _errorState.value = "Error upvoting: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun downvoteReply(commentId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("ForumPageVM", "Downvoting comment: $commentId")
                val success = container.voteRepository.addDownvote(commentId, currentUserId)
                if (success) {
                    Log.d("ForumPageVM", "Comment downvoted successfully")
                    // Reload the selected post to refresh comment votes
                    _selectedPost.value?.let { post ->
                        selectPost(post.id)
                    }
                    loadData()
                } else {
                    _errorState.value = "Failed to downvote comment"
                }
            } catch (e: Exception) {
                Log.e("ForumPageVM", "Error downvoting comment", e)
                _errorState.value = "Error downvoting: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Add new comment to post
    fun sendReply(content: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val postId = _selectedPost.value?.id ?: return@launch
                Log.d("ForumPageVM", "Adding comment to post: $postId")

                val success = container.commentRepository.addComment(postId, content)
                if (success) {
                    Log.d("ForumPageVM", "Comment added successfully")
                    // Reload post to show new comment
                    selectPost(postId)
                    loadData()
                } else {
                    _errorState.value = "Failed to add comment"
                }
            } catch (e: Exception) {
                Log.e("ForumPageVM", "Error adding comment", e)
                _errorState.value = "Error adding comment: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun registerToEvent(eventId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("ForumPageViewModel", "Registering to event: $eventId")
                container.eventRepository.registerToEvent(eventId, currentUserId)
                // Add to registered events set
                _registeredEvents.value = _registeredEvents.value + eventId
                Log.d("ForumPageViewModel", "Successfully registered to event: $eventId")
                // Refresh data to get updated registration count
                loadData()
            } catch (e: Exception) {
                Log.e("ForumPageViewModel", "Error registering to event", e)
                _errorState.value = "Failed to register: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun formatDurationShort(eventDate: Instant): String {
        val now = Instant.now()
        val duration = Duration.between(now, eventDate)

        if (duration.isNegative) return "Ended"

        val totalMinutes = duration.toMinutes()
        val days = totalMinutes / (60 * 24)
        val hours = (totalMinutes % (60 * 24)) / 60
        val minutes = totalMinutes % 60

        return when {
            days > 0 -> "${days}d ${hours}h"
            hours > 0 -> "${hours}h ${minutes}m"
            else -> "${minutes}m"
        }
    }

    // Fungsi untuk Post Detail
    fun selectPost(postId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null
            try {
                Log.d("ForumPageVM", "Selecting post: $postId")

                // First try to find in cached posts
                var post = _posts.value.find { it.id == postId }

                // If not found, load from backend
                if (post == null) {
                    Log.d("ForumPageVM", "Post not in cache, loading from backend...")
                    post = container.postRepository.getPostById(postId)
                    Log.d("ForumPageVM", "Post loaded from backend: $post")
                }

                _selectedPost.value = post
                // Load replies dari comments yang ada di post
                _selectedPostReplies.value = post?.comments ?: emptyList()
                // Compute and set time-ago string for the UI
                _selectedPostTimeAgo.value = post?.let { computeTimeAgo(it.createdAt) } ?: ""

                Log.d("ForumPageVM", "Post selected successfully with ${post?.comments?.size ?: 0} comments")
            } catch (e: Exception) {
                Log.e("ForumPageVM", "Error selecting post", e)
                _errorState.value = "Failed to load post: ${e.message}"
                _selectedPost.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Compute a short "time ago" string from a creation Instant (moved from PostDetail)
    private fun computeTimeAgo(createdAt: Instant): String {
        val now = Instant.now()
        val duration = Duration.between(createdAt, now)

        return when {
            duration.toMinutes() < 1 -> "just now"
            duration.toMinutes() < 60 -> "${duration.toMinutes()}m ago"
            duration.toHours() < 24 -> "${duration.toHours()}h ago"
            duration.toDays() < 7 -> "${duration.toDays()}d ago"
            else -> "${duration.toDays() / 7}w ago"
        }
    }

    fun upvotePost(postId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val post = _posts.value.find { it.id == postId }
                post?.comments?.firstOrNull()?.let { comment ->
                    val success = container.voteRepository.addUpvote(comment.id, currentUserId)
                    if (success) {
                        selectPost(postId)
                        loadData()
                    } else {
                        _errorState.value = "Failed to upvote post"
                    }
                } ?: run {
                    _errorState.value = "Post or comment not found"
                }
            } catch (e: Exception) {
                Log.e("ForumPageVM", "Error upvoting post detail", e)
                _errorState.value = "Error upvoting post: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun downvotePost(postId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val post = _posts.value.find { it.id == postId }
                post?.comments?.firstOrNull()?.let { comment ->
                    val success = container.voteRepository.addDownvote(comment.id, currentUserId)
                    if (success) {
                        selectPost(postId)
                        loadData()
                    } else {
                        _errorState.value = "Failed to downvote post"
                    }
                } ?: run {
                    _errorState.value = "Post or comment not found"
                }
            } catch (e: Exception) {
                Log.e("ForumPageVM", "Error downvoting post detail", e)
                _errorState.value = "Error downvoting post: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
