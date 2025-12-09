package com.jason.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.ui.model.EventPost
import com.jason.alp_vp.ui.model.Post
import com.jason.alp_vp.ui.model.Vote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * ForumPageViewModel holds dummy events and posts.
 * It computes vote totals for each post and exposes PostUi for the UI.
 */
class ForumPageViewModel : ViewModel() {

    // existing event posts flow (kept for UI)
    private val _eventPosts = MutableStateFlow<List<EventPost>>(emptyList())
    val eventPosts: StateFlow<List<EventPost>> = _eventPosts

    // raw posts with votes
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    // UI-friendly posts with computed counts
    data class PostUi(
        val post: Post,
        val upvoteCount: Int,
        val downvoteCount: Int
    )

    private val _postUis = MutableStateFlow<List<PostUi>>(emptyList())
    val postUis: StateFlow<List<PostUi>> = _postUis

    init {
        loadDummyEvents()
        loadDummyPosts()
    }

    private fun loadDummyEvents() {
        viewModelScope.launch {
            val now = Instant.now()
            val dummy = listOf(
                EventPost(
                    id = "evt1",
                    title = "🎯 Tech Startup Hiring Event",
                    organizer = "InnovateTech Corp",
                    description = "Join us for an exclusive hiring event! We're looking for talented developers, designers, and marketers. Register now for priority access to our bounties!",
                    registered = 45,
                    capacity = 100,
                    badgeEmoji = "📅",
                    timeRemaining = Duration.ofHours(2).plusMinutes(13),
                    createdAt = now
                ),
                EventPost(
                    id = "evt2",
                    title = "🚀 Freelancer Networking Session",
                    organizer = "SideQuest Community",
                    description = "Connect with other freelancers, share tips, and learn about upcoming opportunities. Special guest speakers from top companies!",
                    registered = 78,
                    capacity = 150,
                    badgeEmoji = "📅",
                    timeRemaining = Duration.ofHours(5).plusMinutes(29),
                    createdAt = now.minusSeconds(3600)
                )
            )

            _eventPosts.update { dummy }
        }
    }

    private fun loadDummyPosts() {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val p1Votes = listOf(
                Vote(id = UUID.randomUUID().toString(), voteType = "upvote", createdAt = now - 60_000),
                Vote(id = UUID.randomUUID().toString(), voteType = "upvote", createdAt = now - 120_000),
                Vote(id = UUID.randomUUID().toString(), voteType = "downvote", createdAt = now - 30_000)
            )
            val p2Votes = listOf(
                Vote(id = UUID.randomUUID().toString(), voteType = "upvote", createdAt = now - 300_000)
            )

            val posts = listOf(
                Post(id = "p1", content = "Just shipped a small lib for animations — feedback welcome!", createdAt = now - 3_600_000, votes = p1Votes),
                Post(id = "p2", content = "Looking for UI feedback on my new design system.", createdAt = now - 7_200_000, votes = p2Votes)
            )

            _posts.update { posts }
            recomputePostUis(posts)
        }
    }

    private fun recomputePostUis(posts: List<Post>) {
        val ui = posts.map { post ->
            val up = post.votes.count { it.voteType.equals("upvote", ignoreCase = true) }
            val down = post.votes.count { it.voteType.equals("downvote", ignoreCase = true) }
            PostUi(post = post, upvoteCount = up, downvoteCount = down)
        }
        _postUis.update { ui }
    }

    /**
     * Add an upvote to a post (creates a Vote with unique id)
     */
    fun upvote(postId: String) {
        viewModelScope.launch {
            // Build updated list explicitly, set it, then recompute UI counts
            val newList = _posts.value.map { post ->
                if (post.id == postId) {
                    val newVote = Vote(id = UUID.randomUUID().toString(), voteType = "upvote", createdAt = System.currentTimeMillis())
                    post.copy(votes = post.votes + newVote)
                } else post
            }
            _posts.value = newList
            recomputePostUis(newList)
        }
    }

    /**
     * Add a downvote to a post (creates a Vote with unique id)
     */
    fun downvote(postId: String) {
        viewModelScope.launch {
            val newList = _posts.value.map { post ->
                if (post.id == postId) {
                    val newVote = Vote(id = UUID.randomUUID().toString(), voteType = "downvote", createdAt = System.currentTimeMillis())
                    post.copy(votes = post.votes + newVote)
                } else post
            }
            _posts.value = newList
            recomputePostUis(newList)
        }
    }

    // helper to simulate adding/removing for testing
    fun registerToEvent(eventId: String) {
        _eventPosts.update { list ->
            list.map {
                if (it.id == eventId && it.registered < it.capacity) {
                    it.copy(registered = it.registered + 1)
                } else it
            }
        }
    }
}
