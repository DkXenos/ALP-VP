package com.jason.alp_vp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
import com.jason.alp_vp.ui.model.Comment
import com.jason.alp_vp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReplyViewModel(
    private val container: AppContainer = AppContainer()
) : ViewModel() {

    private val _replies = MutableStateFlow<List<Comment>>(emptyList())
    val replies: StateFlow<List<Comment>> = _replies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val currentUserId: Int
        get() = try {
            val userId = TokenManager.getUserId()
            if (userId > 0) userId else 1 // Use 1 as fallback if invalid ID
        } catch (e: Exception) {
            Log.e("ReplyViewModel", "Error getting user ID", e)
            1 // Fallback user ID
        }

    private var currentPostId: Int? = null

    fun loadRepliesForPost(postId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            currentPostId = postId

            try {
                val comments = container.commentRepository.getCommentsByPost(postId)
                _replies.value = comments
            } catch (e: Exception) {
                Log.e("ReplyViewModel", "Failed to load replies", e)
                _error.value = "Failed to load replies: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createReply(postId: Int, content: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newComment = container.commentRepository.createComment(
                    postId = postId,
                    userId = currentUserId,
                    content = content
                )
                // Refresh the replies list
                loadRepliesForPost(postId)
            } catch (e: Exception) {
                Log.e("ReplyViewModel", "Failed to create reply", e)
                _error.value = "Failed to create reply: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun upvoteComment(commentId: Int) {
        viewModelScope.launch {
            try {
                val success = container.voteRepository.addUpvote(commentId, currentUserId)
                if (success) {
                    // Refresh comments to get updated vote counts
                    refreshCurrentReplies()
                } else {
                    _error.value = "Failed to upvote comment"
                }
            } catch (e: Exception) {
                Log.e("ReplyViewModel", "Error upvoting comment", e)
                _error.value = "Error upvoting: ${e.message}"
            }
        }
    }

    fun downvoteComment(commentId: Int) {
        viewModelScope.launch {
            try {
                val success = container.voteRepository.addDownvote(commentId, currentUserId)
                if (success) {
                    // Refresh comments to get updated vote counts
                    refreshCurrentReplies()
                } else {
                    _error.value = "Failed to downvote comment"
                }
            } catch (e: Exception) {
                Log.e("ReplyViewModel", "Error downvoting comment", e)
                _error.value = "Error downvoting: ${e.message}"
            }
        }
    }

    private fun refreshCurrentReplies() {
        currentPostId?.let { postId ->
            loadRepliesForPost(postId)
        }
    }

    fun clearError() {
        _error.value = null
    }
}
