package com.jason.alp_vp.ui.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import java.time.Duration
import java.time.Instant

// Colors
private val Background = Color(0xFF0F1115)
private val CardBackground = Color(0xFF14161A)
private val TitleColor = Color(0xFFFFFFFF)
private val SubText = Color(0xFF98A0B3)
private val AccentBlue = Color(0xFF2F6BFF)
private val AccentGreen = Color(0xFF57D06A)
private val AccentRed = Color(0xFFF85C5C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostPage(
    viewModel: ForumPageViewModel = viewModel(),
    onBack: () -> Unit = {},
    onNavigateToPostDetail: (Int) -> Unit = {}
) {
    val postUis by viewModel.postUis.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    // Log for debugging
    LaunchedEffect(postUis) {
        Log.d("PostPage", "Total posts: ${postUis.size}")
        if (postUis.isEmpty()) {
            Log.w("PostPage", "No posts to display!")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Community Feed",
                        color = TitleColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TitleColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
                )
            )
        },
        containerColor = Background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Error message
            errorState?.let { error ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AccentRed.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚠️",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Failed to Load Posts",
                                color = AccentRed,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = error,
                                color = TitleColor,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    viewModel.clearError()
                                    viewModel.refreshData()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }

            // Empty state
            if (postUis.isEmpty() && !isLoading && errorState == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "💬",
                        fontSize = 72.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "No Posts Yet",
                        color = TitleColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Be the first to share something\nwith the community!",
                        color = SubText,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.refreshData() },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                    ) {
                        Text("Refresh")
                    }
                }
            }

            // Posts list
            if (postUis.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        Text(
                            text = "${postUis.size} posts",
                            color = SubText,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    items(postUis, key = { it.post.id }) { postUi ->
                        FullPostCard(
                            id = postUi.post.id,
                            authorName = postUi.post.authorName,
                            content = postUi.post.content,
                            image = postUi.post.image,
                            createdAt = postUi.post.createdAt,
                            upvoteCount = postUi.upvoteCount,
                            downvoteCount = postUi.downvoteCount,
                            commentCount = postUi.post.comments.size,
                            onUpvote = { viewModel.upvote(postUi.post.id) },
                            onDownvote = { viewModel.downvote(postUi.post.id) },
                            onClick = { onNavigateToPostDetail(postUi.post.id) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }

            // Loading overlay
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Background.copy(alpha = 0.8f)),
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
                                text = "Loading posts...",
                                color = TitleColor,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FullPostCard(
    id: Int,
    authorName: String,
    content: String,
    image: String?,
    createdAt: Instant,
    upvoteCount: Int,
    downvoteCount: Int,
    commentCount: Int,
    onUpvote: () -> Unit,
    onDownvote: () -> Unit,
    onClick: () -> Unit
) {
    val timeAgo = remember(createdAt) {
        val duration = Duration.between(createdAt, Instant.now())
        when {
            duration.toMinutes() < 1 -> "just now"
            duration.toMinutes() < 60 -> "${duration.toMinutes()}m ago"
            duration.toHours() < 24 -> "${duration.toHours()}h ago"
            duration.toDays() < 7 -> "${duration.toDays()}d ago"
            else -> "${duration.toDays() / 7}w ago"
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Author info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(AccentBlue, RoundedCornerShape(22.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = authorName.take(1).uppercase(),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = authorName,
                        color = TitleColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = timeAgo,
                        color = SubText,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            Text(
                text = content,
                color = TitleColor,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            // Image if present
            if (!image.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(SubText.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🖼️ Image",
                        color = SubText,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Divider
            Divider(color = SubText.copy(alpha = 0.2f), thickness = 1.dp)

            Spacer(modifier = Modifier.height(12.dp))

            // Actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Upvote
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onUpvote) {
                        Text("▲", color = AccentGreen, fontSize = 20.sp)
                    }
                    Text(
                        text = upvoteCount.toString(),
                        color = SubText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Downvote
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDownvote) {
                        Text("▼", color = AccentRed, fontSize = 20.sp)
                    }
                    Text(
                        text = downvoteCount.toString(),
                        color = SubText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Comments
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "💬",
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$commentCount",
                        color = SubText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

