package com.jason.alp_vp.ui.viewmodel

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

    private val currentUserId = 1 // Mock current user ID

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                // TODO: Load events from backend when EventRepository is ready
                _events.value = emptyList()

                // TODO: Load posts from backend when PostRepository is ready
                _posts.value = emptyList()

                recomputePostUis(emptyList())
            } catch (e: Exception) {
                // Handle error
                _events.value = emptyList()
                _posts.value = emptyList()
            }
        }
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
