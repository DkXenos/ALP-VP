package com.jason.alp_vp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jason.alp_vp.ui.model.Comment
import com.jason.alp_vp.ui.model.CommentVote
import com.jason.alp_vp.ui.view.ReplyItem
import java.time.temporal.ChronoUnit

@Composable
fun RepliesScreen(
    modifier: Modifier = Modifier,
    replies: List<Comment> = emptyList(),
    isLoading: Boolean = false,
    error: String? = null,
    onUpvote: (Int) -> Unit = {},
    onDownvote: (Int) -> Unit = {},
    onClearError: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Replies (${replies.size})",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Error handling
        error?.let { errorMessage ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3D1A1A)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = errorMessage,
                        color = Color(0xFFFF5C5C),
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = onClearError) {
                        Text("Dismiss", color = Color(0xFFFF5C5C))
                    }
                }
            }
        }

        // Loading state
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF1DB954),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Replies list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(replies) { comment ->
                ReplyItem(
                    comment = comment,
                    authorName = "User${comment.id}",
                    onUpvote = onUpvote,
                    onDownvote = onDownvote
                )
            }
        }

        // Empty state
        if (!isLoading && replies.isEmpty() && error == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No replies yet. Be the first to reply!",
                    color = Color(0xFF98A0B3),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepliesScreenPreview() {
    val now = java.time.Instant.now().minus(2, ChronoUnit.HOURS)
    val sampleReplies = listOf(
        Comment(
            id = 1,
            postId = 1,
            content = "Great post! Very informative.",
            createdAt = now,
            commentVotes = listOf(
                CommentVote(commentId = 1, voteId = 1, voteType = "upvote"),
                CommentVote(commentId = 1, voteId = 2, voteType = "upvote")
            )
        ),
        Comment(
            id = 2,
            postId = 1,
            content = "Thanks for sharing this!",
            createdAt = now.minus(1, ChronoUnit.HOURS),
            commentVotes = listOf(
                CommentVote(commentId = 2, voteId = 3, voteType = "upvote")
            )
        )
    )

    RepliesScreen(
        replies = sampleReplies
    )
}
