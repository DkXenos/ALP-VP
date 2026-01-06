package com.jason.alp_vp.data.repository

import android.util.Log
import com.jason.alp_vp.data.service.VoteService
import com.jason.alp_vp.data.dto.vote.CreateVoteRequest
import com.jason.alp_vp.data.dto.comment.LinkCommentVoteRequest

class VoteRepository(private val service: VoteService) {

    suspend fun addUpvote(commentId: Int, userId: Int): Boolean {
        return try {
            val request = CreateVoteRequest(
                user_id = userId,
                vote_type = "upvote"
            )
            val response = service.createVote(request)

            if (response.isSuccessful && response.body()?.data != null) {
                // Link the vote to the comment
                val voteId = response.body()!!.data!!.id
                linkVoteToComment(commentId, voteId)
            } else {
                Log.e("VoteRepository", "Failed to create upvote: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e("VoteRepository", "Error adding upvote", e)
            false
        }
    }

    suspend fun addDownvote(commentId: Int, userId: Int): Boolean {
        return try {
            val request = CreateVoteRequest(
                user_id = userId,
                vote_type = "downvote"
            )
            val response = service.createVote(request)

            if (response.isSuccessful && response.body()?.data != null) {
                // Link the vote to the comment
                val voteId = response.body()!!.data!!.id
                linkVoteToComment(commentId, voteId)
            } else {
                Log.e("VoteRepository", "Failed to create downvote: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e("VoteRepository", "Error adding downvote", e)
            false
        }
    }

    private suspend fun linkVoteToComment(commentId: Int, voteId: Int): Boolean {
        return try {
            val commentRequest = LinkCommentVoteRequest(
                comment_id = commentId,
                vote_id = voteId
            )
            val response = service.linkCommentVote(commentRequest)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("VoteRepository", "Error linking vote to comment", e)
            false
        }
    }

    suspend fun removeVote(voteId: Int): Boolean {
        return try {
            val response = service.deleteVote(voteId)
            if (response.isSuccessful) {
                Log.d("VoteRepository", "Vote $voteId deleted successfully")
                true
            } else {
                Log.e("VoteRepository", "Failed to delete vote: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e("VoteRepository", "Error removing vote", e)
            false
        }
    }
}
