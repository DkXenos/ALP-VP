package com.jason.alp_vp.data.repository

import android.util.Log
import com.jason.alp_vp.data.dto.post.PostResponseItem
import com.jason.alp_vp.data.service.PostService
import com.jason.alp_vp.ui.model.Post
import com.jason.alp_vp.ui.model.Comment
import com.jason.alp_vp.ui.model.CommentVote
import java.time.Instant
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PostRepository(
    private val service: PostService,
    private val commentRepository: CommentRepository
) {

    suspend fun getAllPosts(): List<Post> = coroutineScope {
        val response = service.getAllPosts()
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            Log.e("PostRepository", "Failed to fetch posts: ${response.code()} - $msg")
            throw Exception("Failed to fetch posts: ${response.code()} - $msg")
        }
        val wrapper = response.body()
        if (wrapper == null) {
            Log.e("PostRepository", "Response body is null")
            throw Exception("Empty response from server")
        }
        val items = wrapper.data  // Unwrap from { "data": [...] }
        Log.d("PostRepository", "Received ${items.size} posts from API")

        items.map { item ->
            async { dtoToUi(item) }
        }.awaitAll()
    }

    suspend fun getPostById(id: Int): Post {
        val response = service.getPostById(id)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to fetch post: ${response.code()} - $msg")
        }
        val wrapper = response.body() ?: throw IllegalStateException("Empty post response")
        val item = wrapper.data  // Unwrap from { "data": {...} }
        return dtoToUi(item)
    }

    private suspend fun dtoToUi(item: PostResponseItem): Post {
        // Convert embedded comments from API response
        val comments = item.comments?.map { commentItem ->
            Comment(
                id = commentItem.id,
                postId = commentItem.post_id,
                content = commentItem.content,
                createdAt = try { Instant.parse(commentItem.created_at) } catch (_: Exception) { Instant.now() },
                commentVotes = commentItem.commentVotes?.map { vote ->
                    CommentVote(
                        commentId = commentItem.id,
                        voteId = vote.id,
                        voteType = vote.vote_type
                    )
                } ?: emptyList()
            )
        } ?: emptyList()

        return Post(
            id = item.id,
            userId = item.user_id,
            authorName = item.username,  // Flattened - direct from root
            authorEmail = "",  // Not provided in flattened API response
            content = item.content,
            image = item.image,
            createdAt = try { Instant.parse(item.created_at) } catch (_: Exception) { Instant.now() },
            comments = comments
        )
    }
}
