package com.jason.alp_vp.ui.view

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.ui.theme.*
import com.jason.alp_vp.ui.viewmodel.ForumPageViewModel


@Composable
fun ForumPage(
    viewModel: ForumPageViewModel = viewModel(),
    onNavigateToEventPage: () -> Unit = {},
    onNavigateToPostPage: () -> Unit = {},
    onNavigateToPostDetail: (Int) -> Unit = {},
    onNavigateToEventDetail: (Int) -> Unit = {}
) {
    val events by viewModel.events.collectAsState()
    val postUis by viewModel.postUis.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorState by viewModel.errorState.collectAsState()
    val registeredEvents by viewModel.registeredEvents.collectAsState()

    // Tab state: 0 = Events, 1 = Posts
    var selectedTab by remember { mutableStateOf(0) }

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
            .background(BackgroundDark)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Tab Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Events Tab
                TabButton(
                    text = "Events (${events.size})",
                    isSelected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.weight(1f)
                )

                // Posts Tab
                TabButton(
                    text = "Posts (${postUis.size})",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    modifier = Modifier.weight(1f)
                )
            }

            // Content based on selected tab
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(vertical = 20.dp)
            ) {
            // Error message if any
            errorState?.let { error ->
                item {
                    GlowingCard(
                        glowColor = StatusError.copy(alpha = 0.4f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚠️ Error Loading Data",
                                color = StatusError,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = error,
                                color = TextPrimary,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            GradientButton(
                                text = "Retry",
                                onClick = { viewModel.refreshData() }
                            )
                        }
                    }
                }
            }

            // Show content based on selected tab
            if (selectedTab == 0) {
                // Events Tab Content
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Upcoming Events",
                            color = TextPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        TextButton(onClick = onNavigateToEventPage) {
                            Text(
                                text = "View All",
                                color = AccentCyan,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
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
                    items(events) { event ->
                        EventCard(
                            event = event,
                            modifier = Modifier.padding(horizontal = 20.dp),
                            isRegistered = registeredEvents.contains(event.id),
                            onRegister = { viewModel.registerToEvent(it) },
                            onClick = { onNavigateToEventDetail(event.id) }
                        )
                    }
                }
            } else {
                // Posts Tab Content
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Community Posts",
                            color = TextPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        TextButton(onClick = onNavigateToPostPage) {
                            Text(
                                text = "View All",
                                color = AccentCyan,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
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
                    items(postUis) { postUi ->
                        PostCardImproved(
                            id = postUi.post.id,
                            authorName = postUi.post.authorName,
                            content = postUi.post.content,
                            createdAt = postUi.post.createdAt,
                            upvoteCount = postUi.upvoteCount,
                            downvoteCount = postUi.downvoteCount,
                            commentCount = postUi.post.comments.size,
                            onClick = { onNavigateToPostDetail(postUi.post.id) },
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }

        // Loading overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundDark.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                GlowingCard {
                    Column(
                        modifier = Modifier.padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = AccentCyan,
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Loading...",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
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
    GlowingCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 56.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = message,
                color = TextSecondary,
                fontSize = 15.sp,
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

    GlowingCard(
        modifier = modifier.fillMaxWidth(),
        glowColor = BorderGlow
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(20.dp)
        ) {
            // Header: Author and timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar with gradient
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                androidx.compose.ui.graphics.Brush.linearGradient(
                                    colors = listOf(AccentCyan, AccentPurple)
                                ),
                                RoundedCornerShape(22.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = authorName.take(1).uppercase(),
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = authorName,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = timeAgo,
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Content
            Text(
                text = content,
                color = TextPrimary,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                maxLines = 4,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Actions: Upvote, Downvote, Comments (display only - voting not supported)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Upvote (display only)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = StatusSuccess.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, StatusSuccess.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("▲", color = StatusSuccess.copy(alpha = 0.5f), fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = upvoteCount.toString(),
                            color = StatusSuccess,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Downvote (display only)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = StatusError.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, StatusError.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("▼", color = StatusError.copy(alpha = 0.5f), fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = downvoteCount.toString(),
                            color = StatusError,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Comments
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AccentCyan.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, AccentCyan.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💬",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$commentCount",
                            color = AccentCyan,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) AccentCyan.copy(alpha = 0.2f) else SurfaceDark,
            contentColor = if (isSelected) AccentCyan else TextSecondary
        ),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) {
            BorderStroke(2.dp, AccentCyan)
        } else {
            BorderStroke(1.dp, BorderGlow)
        }
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
