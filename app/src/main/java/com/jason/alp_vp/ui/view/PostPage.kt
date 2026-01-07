package com.jason.alp_vp.ui.view

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.ui.theme.*
import com.jason.alp_vp.ui.viewmodel.ForumPageViewModel
import java.time.Duration
import java.time.Instant


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
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = AccentCyan
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceDarkElevated
                )
            )
        },
        containerColor = BackgroundDark
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
                    GlowingCard(glowColor = StatusError.copy(alpha = 0.4f)) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚠️",
                                fontSize = 56.sp
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Failed to Load Posts",
                                color = StatusError,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = error,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            GradientButton(
                                text = "Retry",
                                onClick = {
                                    viewModel.clearError()
                                    viewModel.refreshData()
                                }
                            )
                        }
                    }
                }
            }

            // Empty state
            if (postUis.isEmpty() && !isLoading && errorState == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "💬",
                        fontSize = 80.sp
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "No Posts Yet",
                        color = TextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Be the first to share something\nwith the community!",
                        color = TextSecondary,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    GradientButton(
                        text = "Refresh",
                        onClick = { viewModel.refreshData() }
                    )
                }
            }

            // Posts list
            if (postUis.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(20.dp)
                ) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AccentCyan.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, AccentCyan.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "${postUis.size} posts",
                                color = AccentCyan,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
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
                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }
            }

            // Loading overlay
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundDark.copy(alpha = 0.85f)),
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
                                text = "Loading posts...",
                                color = TextPrimary,
                                fontSize = 17.sp,
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

    GlowingCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = BorderGlow
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header: Author info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar with gradient glow
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(AccentCyan, AccentPurple)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = authorName.take(1).uppercase(),
                            color = TextPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = authorName,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = timeAgo,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content
            Text(
                text = content,
                color = TextPrimary,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Normal
            )

            // Image if present
            if (!image.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    TextTertiary.copy(alpha = 0.1f),
                                    TextTertiary.copy(alpha = 0.05f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🖼️ Image",
                        color = TextSecondary,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Divider with glow
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BorderGlow)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Actions row with styled buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Upvote
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = StatusSuccess.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, StatusSuccess.copy(alpha = 0.25f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onUpvote,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Text("▲", color = StatusSuccess, fontSize = 18.sp)
                        }
                        Text(
                            text = upvoteCount.toString(),
                            color = StatusSuccess,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Downvote
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = StatusError.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, StatusError.copy(alpha = 0.25f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onDownvote,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Text("▼", color = StatusError, fontSize = 18.sp)
                        }
                        Text(
                            text = downvoteCount.toString(),
                            color = StatusError,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Comments
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AccentCyan.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, AccentCyan.copy(alpha = 0.25f)),
                    onClick = onClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💬",
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$commentCount",
                            color = AccentCyan,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

