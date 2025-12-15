package com.jason.alp_vp.ui.model

import com.jason.alp_vp.data.dto.vote.VoteResponseItem
import java.time.Instant

data class Vote(
    val id: Int,
    val commentId: Int,
    val userId: Int,
    val voteType: String, // 'upvote' or 'downvote'
    val createdAt: Instant = Instant.now()
)

// Extension function to convert DTO to Model
@Suppress("unused")
fun VoteResponseItem.toModel() = Vote(
    id = this.id,
    commentId = this.commentId,
    userId = this.userId,
    voteType = this.voteType,
    createdAt = try {
        Instant.parse(this.createdAt)
    } catch (_: Exception) {
        Instant.now()
    }
)
