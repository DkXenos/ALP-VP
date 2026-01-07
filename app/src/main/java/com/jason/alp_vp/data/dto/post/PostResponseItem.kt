package com.jason.alp_vp.data.dto.post

data class PostResponseItem(
    val id: Int,
    val user_id: Int,
    val username: String,  // Flattened - NOT nested in author object
    val content: String,
    val image: String?,
    val created_at: String,
    val comments: List<CommentItem>? = null
)

data class CommentItem(
    val id: Int,
    val post_id: Int,
    val content: String,
    val created_at: String,
    val commentVotes: List<CommentVote>? = null
)

data class CommentVote(
    val id: Int,
    val vote_type: String,  // "upvote" or "downvote"
    val comment_id: Int
)