package com.jason.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.data.container.AppContainer
import com.jason.alp_vp.ui.model.Comment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReplyViewModel(
    private val container: AppContainer,
    private val currentUserId: Int = 1 // TODO: Get from user session
) : ViewModel() {

    private val _replies = MutableStateFlow<List<Comment>>(emptyList())
    val replies: StateFlow<List<Comment>> = _replies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadRepliesForPost(postId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val comments = container.commentRepository.getCommentsByPost(postId)
                _replies.value = comments
            } catch (e: Exception) {
                _error.value = "Failed to load replies: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createReply(postId: Int, content: String) {
        viewModelScope.launch {
            try {
                val newComment = container.commentRepository.createComment(
                    postId = postId,
                    userId = currentUserId,
                    content = content
                )
                // Refresh the replies list
                loadRepliesForPost(postId)
            } catch (e: Exception) {
                _error.value = "Failed to create reply: ${e.message}"
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
                _error.value = "Error downvoting: ${e.message}"
            }
        }
    }

    private fun refreshCurrentReplies() {
        // This assumes we're tracking the current postId
        // In a real app, you'd want to store the current postId
        // For now, this is a placeholder - you'd call loadRepliesForPost(currentPostId)
    }

    fun clearError() {
        _error.value = null
    }
}
