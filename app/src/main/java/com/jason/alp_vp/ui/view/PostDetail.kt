package com.jason.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import com.jason.alp_vp.ui.viewmodel.ForumPageViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState

@Composable
fun PostDetail(
    post: Post,
    authorName: String = "John Developer",
    authorInitial: String = authorName.firstOrNull()?.uppercaseChar()?.toString() ?: "J",
    replies: List<Post> = emptyList(),
    onUpvotePost: (Int) -> Unit = {},
    onDownvotePost: (Int) -> Unit = {},
    onUpvoteReply: (Int) -> Unit = {},
    onDownvoteReply: (Int) -> Unit = {},
    onSendReply: (String) -> Unit = {}
) {
    var replyText by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF0F1115)) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    // Hitung votes dari comments
                    val upvotes = 0
                    val downvotes = 0
                    // TODO: Implement vote counting from post.comments

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
                                        Text(text = "2h ago", color = Color(0xFF98A0B3), fontSize = 12.sp)
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = post.content,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        maxLines = 6,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Upvote button
                                        Button (
                                            onClick = { onUpvotePost(post.id) },
                                            colors = ButtonDefaults.buttonColors(Color(0xFF17291E)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 1.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowUp,
                                                contentDescription = "Upvote",
                                                tint = Color(0xFF57D06A),
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = upvotes.toString(), color = Color(0xFF57D06A), fontSize = 13.sp)
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        // Downvote button
                                        Button (
                                            onClick = { onDownvotePost(post.id) },
                                            colors = ButtonDefaults.buttonColors(Color(0xFF291717)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 1.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowDown,
                                                contentDescription = "Downvote",
                                                tint = Color(0xFFFF5C5C),
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = downvotes.toString(), color = Color(0xFFFF5C5C), fontSize = 13.sp)
                                        }
                                    }
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

                items(replies) { reply ->
                    ReplyItem(
                        reply = reply,
                        authorInitial = reply.content.firstOrNull()?.uppercaseChar()?.toString() ?: "S",
                        authorName = "User",
                        onUpvote = { onUpvoteReply(reply.id) },
                        onDownvote = { onDownvoteReply(reply.id) }
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
                        Text("→", color = Color.White, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun PostDetailScreen(viewModel: ForumPageViewModel = viewModel()) {
    val post = viewModel.selectedPost.collectAsState().value ?: return
    val replies = viewModel.selectedPostReplies.collectAsState().value

    PostDetail(
        post = post,
        replies = replies,
        onUpvotePost = { viewModel.upvotePost(it) },
        onDownvotePost = { viewModel.downvotePost(it) },
        onUpvoteReply = { viewModel.upvoteReply(it) },
        onDownvoteReply = { viewModel.downvoteReply(it) },
        onSendReply = { viewModel.sendReply(it) }
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun PostDetailPreview() {
    val now = java.time.Instant.now()
    val post = com.jason.alp_vp.ui.model.Post(
        id = 1,
        userId = 2,
        content = "Just completed my first bounty! The XP system is so motivating. Anyone else working on the e-commerce project? I found some great libraries for payment integration that really speed up development.",
        image = null,
        createdAt = now.minusSeconds(2_000_000),
        comments = emptyList()
    )

    val r1 = com.jason.alp_vp.ui.model.Post(
        id = 2,
        userId = 3,
        content = "Great job! I'm also working on something similar. Which payment library did you use?",
        image = null,
        createdAt = now.minusSeconds(3_600_000),
        comments = emptyList()
    )

    val r2 = com.jason.alp_vp.ui.model.Post(
        id = 3,
        userId = 4,
        content = "Congrats on your first bounty! The XP system really is addictive 😄",
        image = null,
        createdAt = now.minusSeconds(2_700_000),
        comments = emptyList()
    )

    PostDetail(post = post, replies = listOf(r1, r2))
}
