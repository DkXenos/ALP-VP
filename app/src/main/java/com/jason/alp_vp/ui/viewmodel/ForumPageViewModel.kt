package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
import com.jason.alp_vp.data.repository.EventRepository
import com.jason.alp_vp.data.repository.PostRepository
import com.jason.alp_vp.ui.model.Event
import com.jason.alp_vp.ui.model.Post
import com.jason.alp_vp.ui.model.Comment
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

    private val currentUserId = 1 // Mock current user ID

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
            try {
                // Find post and add upvote to first comment
                val post = _posts.value.find { it.id == postId }
                post?.comments?.firstOrNull()?.let { comment ->
                    container.voteRepository.addUpvote(comment.id, currentUserId)
                    loadData()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun downvote(postId: Int) {
        viewModelScope.launch {
            try {
                val post = _posts.value.find { it.id == postId }
                post?.comments?.firstOrNull()?.let { comment ->
                    container.voteRepository.addDownvote(comment.id, currentUserId)
                    loadData()
                }
            } catch (e: Exception) {
                // Handle error
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
            try {
                val post = _posts.value.find { it.id == postId }
                post?.comments?.firstOrNull()?.let { comment ->
                    container.voteRepository.addUpvote(comment.id, currentUserId)
                    selectPost(postId)
                    loadData()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun downvotePost(postId: Int) {
        viewModelScope.launch {
            try {
                val post = _posts.value.find { it.id == postId }
                post?.comments?.firstOrNull()?.let { comment ->
                    container.voteRepository.addDownvote(comment.id, currentUserId)
                    selectPost(postId)
                    loadData()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun upvoteReply(commentId: Int) {
        viewModelScope.launch {
            try {
                container.voteRepository.addUpvote(commentId, currentUserId)
                _selectedPost.value?.let { selectPost(it.id) }
                loadData()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun downvoteReply(commentId: Int) {
        viewModelScope.launch {
            try {
                container.voteRepository.addDownvote(commentId, currentUserId)
                _selectedPost.value?.let { selectPost(it.id) }
                loadData()
            } catch (e: Exception) {
                // Handle error
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
