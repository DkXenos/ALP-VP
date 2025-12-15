package com.jason.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.ui.model.Event
import com.jason.alp_vp.ui.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant

class ForumPageViewModel : ViewModel() {

    private val eventRepo = EventRepository
    private val postRepo = PostRepository

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

    private val _selectedPostReplies = MutableStateFlow<List<Post>>(emptyList())
    val selectedPostReplies: StateFlow<List<Post>> = _selectedPostReplies

    private val currentUserId = 1 // Mock current user ID

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _events.value = eventRepo.getAllEvents()
            val allPosts = postRepo.getAllPosts()
            _posts.value = allPosts
            recomputePostUis(allPosts)
        }
    }

    private fun recomputePostUis(posts: List<Post>) {
        val ui = posts.map { post ->
            var upvoteCount = 0
            var downvoteCount = 0

            post.comments.forEach { comment ->
                comment.commentVotes.forEach { commentVote ->
                    val vote = postRepo.getVote(commentVote.voteId)
                    when (vote?.voteType) {
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
            // Find first comment in post and add upvote to it
            val post = postRepo.getPost(postId)
            post?.comments?.firstOrNull()?.let { comment ->
                postRepo.addUpvoteToComment(comment.id)
                loadData() // Reload to get updated data
            }
        }
    }

    fun downvote(postId: Int) {
        viewModelScope.launch {
            val post = postRepo.getPost(postId)
            post?.comments?.firstOrNull()?.let { comment ->
                postRepo.addDownvoteToComment(comment.id)
                loadData()
            }
        }
    }

    fun registerToEvent(eventId: Int) {
        viewModelScope.launch {
            val success = eventRepo.register(eventId, currentUserId)
            if (success) {
                _events.value = eventRepo.getAllEvents()
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
            // Load replies (mock data untuk sekarang)
            _selectedPostReplies.value = emptyList()
        }
    }

    fun upvotePost(postId: Int) {
        viewModelScope.launch {
            // Implementasi upvote untuk post
            val post = postRepo.getPost(postId)
            post?.comments?.firstOrNull()?.let { comment ->
                postRepo.addUpvoteToComment(comment.id)
                // Reload selected post
                selectPost(postId)
                loadData()
            }
        }
    }

    fun downvotePost(postId: Int) {
        viewModelScope.launch {
            // Implementasi downvote untuk post
            val post = postRepo.getPost(postId)
            post?.comments?.firstOrNull()?.let { comment ->
                postRepo.addDownvoteToComment(comment.id)
                // Reload selected post
                selectPost(postId)
                loadData()
            }
        }
    }

    fun upvoteReply(replyId: Int) {
        viewModelScope.launch {
            // Implementasi upvote untuk reply
            val reply = postRepo.getPost(replyId)
            reply?.comments?.firstOrNull()?.let { comment ->
                postRepo.addUpvoteToComment(comment.id)
                // Reload replies
                _selectedPost.value?.let { selectPost(it.id) }
            }
        }
    }

    fun downvoteReply(replyId: Int) {
        viewModelScope.launch {
            // Implementasi downvote untuk reply
            val reply = postRepo.getPost(replyId)
            reply?.comments?.firstOrNull()?.let { comment ->
                postRepo.addDownvoteToComment(comment.id)
                // Reload replies
                _selectedPost.value?.let { selectPost(it.id) }
            }
        }
    }

    fun sendReply(content: String) {
        viewModelScope.launch {
            // Implementasi send reply
            _selectedPost.value?.let { post ->
                // TODO: Create new reply post and add to selectedPostReplies
                // For now, just reload
                selectPost(post.id)
            }
        }
    }
}
