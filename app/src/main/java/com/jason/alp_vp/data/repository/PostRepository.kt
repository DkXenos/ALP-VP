package com.jason.alp_vp.data.repository

import com.jason.alp_vp.data.dto.post.PostResponse
import com.jason.alp_vp.data.dto.post.PostResponseItem
import com.jason.alp_vp.data.service.PostService
import com.jason.alp_vp.ui.model.Post
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
            throw Exception("Failed to fetch posts: ${response.code()} - $msg")
        }
        val body = response.body() ?: emptyList()
        // body: List<PostResponse> where PostResponse is ArrayList<PostResponseItem>
        val items = flattenPostResponses(body)

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
        val body = response.body()!!
        val items = flattenPostResponses(listOf(body))
        val first = items.firstOrNull() ?: throw IllegalStateException("Empty post response")
        return dtoToUi(first)
    }

    private suspend fun dtoToUi(item: PostResponseItem): Post {
        val comments = try {
            commentRepository.getCommentsByPost(item.id)
        } catch (_: Exception) {
            emptyList()
        }
        return Post(
            id = item.id,
            userId = item.userId,
            authorName = item.author.username,
            authorEmail = item.author.email,
            content = item.content,
            image = item.image?.toString(),
            createdAt = try { Instant.parse(item.createdAt) } catch (_: Exception) { Instant.now() },
            comments = comments
        )
    }

    // Flatten List<PostResponse> -> List<PostResponseItem>
    private fun flattenPostResponses(listOfResponses: List<PostResponse>): List<PostResponseItem> {
        val out = mutableListOf<PostResponseItem>()
        for (r in listOfResponses) {
            out.addAll(r)
        }
        return out
    }
}
