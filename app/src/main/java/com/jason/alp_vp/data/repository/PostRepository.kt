package com.jason.alp_vp.data.repository

import com.jason.alp_vp.ui.model.Comment
import com.jason.alp_vp.ui.model.CommentVote
import com.jason.alp_vp.ui.model.Post
import com.jason.alp_vp.ui.model.Vote
import java.time.Instant

object PostRepository {
    private var postIdCounter = 1
    private var commentIdCounter = 1
    private var voteIdCounter = 1

    private val votes = mutableMapOf<Int, Vote>()
    private val comments = mutableMapOf<Int, Comment>()
    private val posts = mutableMapOf<Int, Post>()

    init {
        // Create some votes
        val upvote1 = Vote(id = voteIdCounter++, voteType = "upvote")
        val upvote2 = Vote(id = voteIdCounter++, voteType = "upvote")
        val upvote3 = Vote(id = voteIdCounter++, voteType = "upvote")
        val downvote1 = Vote(id = voteIdCounter++, voteType = "downvote")
        val upvote4 = Vote(id = voteIdCounter++, voteType = "upvote")

        votes[upvote1.id] = upvote1
        votes[upvote2.id] = upvote2
        votes[upvote3.id] = upvote3
        votes[downvote1.id] = downvote1
        votes[upvote4.id] = upvote4

        // Create comments with votes
        val comment1 = Comment(
            id = commentIdCounter++,
            postId = 1,
            content = "Great insight! Thanks for sharing.",
            createdAt = Instant.now().minusSeconds(3600),
            commentVotes = listOf(
                CommentVote(commentId = 1, voteId = upvote1.id),
                CommentVote(commentId = 1, voteId = upvote2.id),
                CommentVote(commentId = 1, voteId = downvote1.id)
            )
        )

        val comment2 = Comment(
            id = commentIdCounter++,
            postId = 2,
            content = "Looking forward to this!",
            createdAt = Instant.now().minusSeconds(1800),
            commentVotes = listOf(
                CommentVote(commentId = 2, voteId = upvote4.id)
            )
        )

        comments[comment1.id] = comment1
        comments[comment2.id] = comment2

        // Create posts
        val post1 = Post(
            id = postIdCounter++,
            userId = 1,
            content = "Just shipped a small lib for animations — feedback welcome!",
            image = null,
            createdAt = Instant.now().minusSeconds(7200),
            comments = listOf(comment1)
        )

        val post2 = Post(
            id = postIdCounter++,
            userId = 2,
            content = "Looking for UI feedback on my new design system. Check out the link in my bio!",
            image = null,
            createdAt = Instant.now().minusSeconds(14400),
            comments = listOf(comment2)
        )

        val post3 = Post(
            id = postIdCounter++,
            userId = 3,
            content = "Who's attending the Tech Startup Hiring Event? Let's connect!",
            image = null,
            createdAt = Instant.now().minusSeconds(1800),
            comments = emptyList()
        )

        posts[post1.id] = post1
        posts[post2.id] = post2
        posts[post3.id] = post3
    }

    fun getAllPosts(): List<Post> = posts.values.toList()

    fun getPost(postId: Int): Post? = posts[postId]

    fun getVote(voteId: Int): Vote? = votes[voteId]

    fun addUpvoteToComment(commentId: Int): Boolean {
        val comment = comments[commentId] ?: return false
        val newVote = Vote(id = voteIdCounter++, voteType = "upvote")
        votes[newVote.id] = newVote

        val updatedCommentVotes = comment.commentVotes + CommentVote(commentId, newVote.id)
        val updatedComment = comment.copy(commentVotes = updatedCommentVotes)
        comments[commentId] = updatedComment

        // Update the post with the new comment
        updateCommentInPost(updatedComment)
        return true
    }

    fun addDownvoteToComment(commentId: Int): Boolean {
        val comment = comments[commentId] ?: return false
        val newVote = Vote(id = voteIdCounter++, voteType = "downvote")
        votes[newVote.id] = newVote

        val updatedCommentVotes = comment.commentVotes + CommentVote(commentId, newVote.id)
        val updatedComment = comment.copy(commentVotes = updatedCommentVotes)
        comments[commentId] = updatedComment

        updateCommentInPost(updatedComment)
        return true
    }

    private fun updateCommentInPost(updatedComment: Comment) {
        val post = posts[updatedComment.postId] ?: return
        val updatedComments = post.comments.map {
            if (it.id == updatedComment.id) updatedComment else it
        }
        posts[post.id] = post.copy(comments = updatedComments)
    }
}

