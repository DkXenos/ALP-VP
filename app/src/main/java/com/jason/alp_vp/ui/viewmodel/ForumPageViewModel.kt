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

    private val currentUserId: Int
        get() = try {
            val userId = TokenManager.getUserId()
            if (userId > 0) userId else 1 // Use 1 as fallback if invalid ID
        } catch (e: Exception) {
            Log.e("ForumPageViewModel", "Error getting user ID", e)
            1 // Fallback user ID
        }

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            try {
                // Load events from backend via repository
                val eventsList = try {
                    container.eventRepository.getAllEvents()
                } catch (e: Exception) {
                    Log.e("ForumPageVM", "Failed to load events", e)
                    emptyList<Event>()
                }

                // Load posts from backend via repository
                val postsList = try {
                    container.postRepository.getAllPosts()
                } catch (e: Exception) {
                    Log.e("ForumPageVM", "Failed to load posts", e)
                    emptyList<Post>()
                }

                _events.value = eventsList
                _posts.value = postsList
                recomputePostUis(postsList)

            } catch (e: Exception) {
                Log.e("ForumPageVM", "Unexpected error while loading data", e)
                _errorState.value = "Failed to load data: ${e.message}"
                // Set safe default values
                _events.value = emptyList()
                _posts.value = emptyList()
                _postUis.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Add utility methods for production stability
    fun refreshData() {
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
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Find post and add upvote to first comment
                val post = _posts.value.find { it.id == postId }
                post?.comments?.firstOrNull()?.let { comment ->
                    val success = container.voteRepository.addUpvote(comment.id, currentUserId)
                    if (success) {
                        loadData() // Refresh to get updated vote counts
                    } else {
                        _errorState.value = "Failed to add upvote"
                    }
                } ?: run {
                    _errorState.value = "Post or comment not found"
                }
            } catch (e: Exception) {
                Log.e("ForumPageVM", "Error upvoting post", e)
                _errorState.value = "Error adding upvote: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun downvote(postId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val post = _posts.value.find { it.id == postId }
                post?.comments?.firstOrNull()?.let { comment ->
                    val success = container.voteRepository.addDownvote(comment.id, currentUserId)
                    if (success) {
                        loadData() // Refresh to get updated vote counts
                    } else {
                        _errorState.value = "Failed to add downvote"
                    }
                } ?: run {
                    _errorState.value = "Post or comment not found"
                }
            } catch (e: Exception) {
                Log.e("ForumPageVM", "Error downvoting post", e)
                _errorState.value = "Error adding downvote: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun registerToEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                // TODO: Implement event registration through backend
                loadData()
            } catch (e: Exception) {
                // Handle error
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
            val post = _posts.value.find { it.id == postId }
            _selectedPost.value = post
            // Load replies dari comments yang ada di post
            _selectedPostReplies.value = post?.comments ?: emptyList()
            // Compute and set time-ago string for the UI
            _selectedPostTimeAgo.value = post?.let { computeTimeAgo(it.createdAt) } ?: ""
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

    fun upvoteReply(commentId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = container.voteRepository.addUpvote(commentId, currentUserId)
                if (success) {
                    _selectedPost.value?.let { selectPost(it.id) }
                    loadData()
                } else {
                    _errorState.value = "Failed to upvote reply"
                }
            } catch (e: Exception) {
                Log.e("ForumPageVM", "Error upvoting reply", e)
                _errorState.value = "Error upvoting reply: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun downvoteReply(commentId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = container.voteRepository.addDownvote(commentId, currentUserId)
                if (success) {
                    _selectedPost.value?.let { selectPost(it.id) }
                    loadData()
                } else {
                    _errorState.value = "Failed to downvote reply"
                }
            } catch (e: Exception) {
                Log.e("ForumPageVM", "Error downvoting reply", e)
                _errorState.value = "Error downvoting reply: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sendReply(content: String) {
        viewModelScope.launch {
            try {
                _selectedPost.value?.let { post ->
                    container.commentRepository.createComment(post.id, currentUserId, content)
                    loadData()
                    selectPost(post.id)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
