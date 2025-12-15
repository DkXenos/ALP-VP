package com.jason.alp_vp.ui.model

import com.jason.alp_vp.data.dto.post.PostResponseItem
import java.time.Instant

data class Post(
    val id: Int,
    val userId: Int,
    val authorName: String = "",
    val authorEmail: String = "",
    val content: String,
    val image: String? = null,
    val createdAt: Instant = Instant.now(),
    val comments: List<Comment> = emptyList()
)

// Extension function to convert DTO to Model
fun PostResponseItem.toModel() = Post(
    id = this.id,
    userId = this.userId,
    authorName = this.author.name,
    authorEmail = this.author.email,
    content = this.content,
    image = this.image?.toString(),
    createdAt = try {
        Instant.parse(this.createdAt)
    } catch (e: Exception) {
        Instant.now()
    }
)

