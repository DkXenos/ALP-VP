package com.jason.alp_vp.ui.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.ui.viewmodel.ForumPageViewModel

// Colors (dark theme)
private val Background = Color(0xFF0F1115)
private val CardBackground = Color(0xFF14161A)
private val TitleColor = Color(0xFFFFFFFF)
private val SubText = Color(0xFF98A0B3)
private val AccentBlue = Color(0xFF2F6BFF)
private val AccentGreen = Color(0xFF57D06A)
private val AccentRed = Color(0xFFF85C5C)

@Composable
fun ForumPage(
    viewModel: ForumPageViewModel = viewModel(),
    onNavigateToEventPage: () -> Unit = {},
    onNavigateToPostPage: () -> Unit = {},
    onNavigateToPostDetail: (Int) -> Unit = {}
) {
    val events by viewModel.events.collectAsState()
    val postUis by viewModel.postUis.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    // Log state for debugging
    LaunchedEffect(postUis, events) {
        Log.d("ForumPage", "Posts count: ${postUis.size}")
        Log.d("ForumPage", "Events count: ${events.size}")
        postUis.forEachIndexed { index, postUi ->
            Log.d("ForumPage", "Post $index: id=${postUi.post.id}, content=${postUi.post.content.take(50)}")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Error message if any
            errorState?.let { error ->
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = AccentRed.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚠️ Error Loading Data",
                                color = AccentRed,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = error,
                                color = TitleColor,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { viewModel.refreshData() },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }

            // Event Posts Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Event Posts",
                        color = TitleColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onNavigateToEventPage) {
                        Text(
                            text = "View All (${events.size})",
                            color = AccentBlue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Show events or empty state
            if (events.isEmpty() && !isLoading) {
                item {
                    EmptyStateCard(
                        icon = "📅",
                        title = "No Events Yet",
                        message = "Check back later for upcoming events"
                    )
                }
            } else {
                items(events.take(2)) { event ->
                    EventCard(
                        event = event,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onRegister = { viewModel.registerToEvent(it) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Community Feed Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Community Feed",
                        color = TitleColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onNavigateToPostPage) {
                        Text(
                            text = "View All (${postUis.size})",
                            color = AccentBlue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Show posts or empty state
            if (postUis.isEmpty() && !isLoading) {
                item {
                    EmptyStateCard(
                        icon = "💬",
                        title = "No Posts Yet",
                        message = "Be the first to share something with the community!"
                    )
                }
            } else {
                items(postUis.take(3)) { postUi ->
                    PostCardImproved(
                        id = postUi.post.id,
                        authorName = postUi.post.authorName,
                        content = postUi.post.content,
                        createdAt = postUi.post.createdAt,
                        upvoteCount = postUi.upvoteCount,
                        downvoteCount = postUi.downvoteCount,
                        commentCount = postUi.post.comments.size,
                        onUpvote = { viewModel.upvote(postUi.post.id) },
                        onDownvote = { viewModel.downvote(postUi.post.id) },
                        onClick = { onNavigateToPostDetail(postUi.post.id) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Loading overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = AccentBlue)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading...",
                            color = TitleColor,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStateCard(
    icon: String,
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                color = TitleColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                color = SubText,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PostCardImproved(
    id: Int,
    authorName: String,
    content: String,
    createdAt: java.time.Instant,
    upvoteCount: Int,
    downvoteCount: Int,
    commentCount: Int,
    onUpvote: () -> Unit = {},
    onDownvote: () -> Unit = {},
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val timeAgo = remember(createdAt) {
        val duration = java.time.Duration.between(createdAt, java.time.Instant.now())
        when {
            duration.toMinutes() < 1 -> "just now"
            duration.toMinutes() < 60 -> "${duration.toMinutes()}m ago"
            duration.toHours() < 24 -> "${duration.toHours()}h ago"
            duration.toDays() < 7 -> "${duration.toDays()}d ago"
            else -> "${duration.toDays() / 7}w ago"
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Author and timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(AccentBlue, RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = authorName.take(1).uppercase(),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = authorName,
                            color = TitleColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = timeAgo,
                            color = SubText,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            Text(
                text = content,
                color = TitleColor,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                maxLines = 4,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Actions: Upvote, Downvote, Comments
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Upvote
                    IconButton(onClick = onUpvote) {
                        Text("▲", color = AccentGreen, fontSize = 18.sp)
                    }
                    Text(
                        text = upvoteCount.toString(),
                        color = SubText,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Downvote
                    IconButton(onClick = onDownvote) {
                        Text("▼", color = AccentRed, fontSize = 18.sp)
                    }
                    Text(
                        text = downvoteCount.toString(),
                        color = SubText,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Comments
                    Text(
                        text = "💬",
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$commentCount comments",
                        color = SubText,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

