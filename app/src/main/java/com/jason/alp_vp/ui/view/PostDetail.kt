package com.jason.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jason.alp_vp.ui.model.Post
import com.jason.alp_vp.ui.model.Comment
import com.jason.alp_vp.ui.model.CommentVote
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PostDetail(
    post: Post,
    replies: List<Comment> = emptyList(),
    timeAgo: String = "",
    onUpvoteReply: (Int) -> Unit = {},
    onDownvoteReply: (Int) -> Unit = {},
    onSendReply: (String) -> Unit = {}
) {
    var replyText by remember { mutableStateOf("") }

    // Get author info from post
    val authorName = post.authorName.ifEmpty { "User${post.userId}" }
    val authorInitial = authorName.firstOrNull()?.uppercaseChar()?.toString() ?: "U"

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF0F1115))
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF14161A)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(Color(0xFF2B62FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(authorInitial, color = Color.White, fontSize = 16.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                    Text(authorName, color = Color.White, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(text = timeAgo, color = Color(0xFF98A0B3), fontSize = 12.sp)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = post.content,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${replies.size} Replies",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            items(replies) { comment ->
                ReplyItem(
                    comment = comment,
                    authorName = "User${comment.id}",
                    onUpvote = onUpvoteReply,
                    onDownvote = onDownvoteReply
                )
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = replyText,
                onValueChange = { replyText = it },
                placeholder = { Text("Write a reply...", color = Color(0xFFBFC4CC)) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedContainerColor = Color(0xFF121316),
                    focusedContainerColor = Color(0xFF121316)
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = {
                if (replyText.isNotBlank()) {
                    onSendReply(replyText.trim())
                    replyText = ""
                }
            }, modifier = Modifier.size(48.dp)) {
                // send icon circle
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFF2F6BFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Downvote",
                        tint = Color(0xFFFFFFFF),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PostDetailPreview() {
    val now = java.time.Instant.now()
    val post = Post(
        id = 1,
        userId = 2,
        content = "Just completed my first bounty! The XP system is so motivating. Anyone else working on the e-commerce project? I found some great libraries for payment integration that really speed up development.",
        image = null,
        createdAt = now.minusSeconds(2_000_000),
        comments = emptyList()
    )

    val r1 = Comment(
        id = 2,
        postId = 1,
        content = "Great job! I'm also working on something similar. Which payment library did you use?",
        createdAt = now.minusSeconds(3_600_000),
        commentVotes = listOf(
            CommentVote(commentId = 2, voteId = 1, voteType = "upvote"),
            CommentVote(commentId = 2, voteId = 2, voteType = "upvote")
        )
    )

    val r2 = Comment(
        id = 3,
        postId = 1,
        content = "Congrats on your first bounty! The XP system really is addictive 😄",
        createdAt = now.minusSeconds(2_700_000),
        commentVotes = listOf(
            CommentVote(commentId = 3, voteId = 3, voteType = "upvote")
        )
    )

    // compute preview timeAgo similar to ViewModel
    val duration = java.time.Duration.between(post.createdAt, now)
    val previewTimeAgo = when {
        duration.toMinutes() < 1 -> "just now"
        duration.toMinutes() < 60 -> "${duration.toMinutes()}m ago"
        duration.toHours() < 24 -> "${duration.toHours()}h ago"
        duration.toDays() < 7 -> "${duration.toDays()}d ago"
        else -> "${duration.toDays() / 7}w ago"
    }

    PostDetail(post = post, replies = listOf(r1, r2), timeAgo = previewTimeAgo)
}

