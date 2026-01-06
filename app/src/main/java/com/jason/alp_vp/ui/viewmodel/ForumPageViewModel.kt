package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
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

    // Error state for UI to show fetch problems
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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

    private val currentUserId = 1 // Mock current user ID

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                // Load events from backend via repository
                val eventsList = try {
                    val ev = container.eventRepository.getAllEvents()
                    Log.d("ForumPageVM", "Loaded events count=${ev.size}")
                    ev
                } catch (e: Exception) {
                    Log.e("ForumPageVM", "Failed to load events", e)
                    _error.value = "Failed to load events: ${e.message}"
                    emptyList()
                }

                // Load posts from backend via repository
                val postsList = try {
                    val p = container.postRepository.getAllPosts()
                    Log.d("ForumPageVM", "Loaded posts count=${p.size}")
                    p
                } catch (e: Exception) {
                    Log.e("ForumPageVM", "Failed to load posts", e)
                    _error.value = _error.value?.let { it + "; Failed to load posts: ${e.message}" } ?: "Failed to load posts: ${e.message}"
                    emptyList()
                }

                _events.value = eventsList
                _posts.value = postsList

                // if postsList is empty, log a warning so it's obvious in logcat
                if (postsList.isEmpty()) {
                    Log.w("ForumPageVM", "Posts list is empty after loadData(). This may be expected (no posts) or due to a backend/auth error.")
                }

                recomputePostUis(postsList)
            } catch (e: Exception) {
                Log.e("ForumPageVM", "Unexpected error while loading data", e)
                _events.value = emptyList()
                _posts.value = emptyList()
                _postUis.value = emptyList()
                _error.value = "Unexpected error: ${e.message}"
            }
        }
    }

    private fun recomputePostUis(posts: List<Post>) {
        val ui = posts.map { post ->
            var upvoteCount = 0
            var downvoteCount = 0

            // defensive: ensure comments list non-null
            val comments = post.comments ?: emptyList()

            comments.forEach { comment ->
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