package com.jason.alp_vp.data.dto.post

import com.google.gson.annotations.SerializedName

// Flat structure - backend returns username directly in post object
data class PostResponseItem(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("username") val username: String,  // Flat - NOT nested in user object
    @SerializedName("content") val content: String,
    @SerializedName("image") val image: String?,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("comments") val comments: List<CommentItem>?
)

data class CommentItem(
    @SerializedName("id") val id: Int,
    @SerializedName("post_id") val post_id: Int,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("commentVotes") val commentVotes: List<VoteItem>?
)

data class VoteItem(
    @SerializedName("id") val id: Int,
    @SerializedName("vote_type") val vote_type: String,  // "upvote" or "downvote"
    @SerializedName("comment_id") val comment_id: Int
)

