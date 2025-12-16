package com.jason.alp_vp.data.repository

import com.jason.alp_vp.data.service.VoteService
import com.jason.alp_vp.data.service.AddVoteRequest

class VoteRepository(private val service: VoteService) {

    suspend fun addUpvote(commentId: Int, userId: Int): Boolean {
        return try {
            val request = AddVoteRequest(
                commentId = commentId,
                userId = userId,
                voteType = "upvote"
            )
            val response = service.addVote(request)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun addDownvote(commentId: Int, userId: Int): Boolean {
        return try {
            val request = AddVoteRequest(
                commentId = commentId,
                userId = userId,
                voteType = "downvote"
            )
            val response = service.addVote(request)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun removeVote(voteId: Int): Boolean {
        return try {
            val response = service.removeVote(voteId)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
