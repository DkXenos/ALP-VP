package com.jason.alp_vp.data.repository

import com.jason.alp_vp.data.dto.comment.CommentResponse
import com.jason.alp_vp.data.dto.comment.CommentResponseItem
import com.jason.alp_vp.data.service.CommentService
import com.jason.alp_vp.data.service.CreateCommentRequest
import com.jason.alp_vp.data.service.UpdateCommentRequest
import com.jason.alp_vp.ui.model.CommentVote
import com.jason.alp_vp.ui.model.Comment as UiComment

class CommentRepository(private val service: CommentService) {

    suspend fun createComment(postId: Int, userId: Int, content: String): UiComment {
        val req = CreateCommentRequest(
            postId = postId,
            userId = userId,
            content = content
        )
        val response = service.createComment(req)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to create comment: ${response.code()} - $msg")
        }
        val body = response.body()!!
        // API returns a CommentResponse (which is an ArrayList<CommentResponseItem>).
        val items = flattenCommentResponses(listOf(body))
        val first = items.firstOrNull() ?: throw IllegalStateException("Empty comment response")
        return dtoToUi(first)
    }

    suspend fun getCommentById(id: Int): UiComment {
        val response = service.getCommentById(id)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to fetch comment: ${response.code()} - $msg")
        }
        val body = response.body()!!
        val items = flattenCommentResponses(listOf(body))
        val first = items.firstOrNull() ?: throw IllegalStateException("Empty comment response")
        return dtoToUi(first)
    }

    suspend fun updateComment(id: Int, content: String): UiComment {
        val req = UpdateCommentRequest(content = content)
        val response = service.updateComment(id, req)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to update comment: ${response.code()} - $msg")
        }
        val body = response.body()!!
        val items = flattenCommentResponses(listOf(body))
        val first = items.firstOrNull() ?: throw IllegalStateException("Empty comment response")
        return dtoToUi(first)
    }

    suspend fun deleteComment(id: Int) {
        val response = service.deleteComment(id)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to delete comment: ${response.code()} - $msg")
        }
    }

    suspend fun getCommentsByPost(postId: Int): List<UiComment> {
        val response = service.getCommentsByPostId(postId)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to fetch comments: ${response.code()} - $msg")
        }
        val body = response.body()!!
        // Response is List<CommentResponse> (each CommentResponse is a list of CommentResponseItem)
        val items = flattenCommentResponses(body)
        return items.map { dtoToUi(it) }
    }

    // Flatten List<CommentResponse> (each is ArrayList<CommentResponseItem>) -> List<CommentResponseItem>
    private fun flattenCommentResponses(listOfResponses: List<CommentResponse>): List<CommentResponseItem> {
        val out = mutableListOf<CommentResponseItem>()
        for (r in listOfResponses) {
            out.addAll(r)
        }
        return out
    }

    // Convert DTO -> UI model
    private fun dtoToUi(item: CommentResponseItem): UiComment {
        return UiComment(
            id = item.id,
            postId = item.postId,
            content = item.content,
            createdAt = try { java.time.Instant.parse(item.createdAt) } catch (_: Exception) { java.time.Instant.now() },
            commentVotes = item.votes.map { v -> CommentVote(commentId = item.id, voteId = v.id, voteType = v.voteType) }
        )
    }
}
