// kotlin
package com.jason.alp_vp.ui.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import com.jason.alp_vp.ui.theme.*
import java.time.Duration
import java.time.Instant

@Composable
fun PostCard(
    id: Int,
    content: String,
    createdAt: Instant,
    upvoteCount: Int,
    downvoteCount: Int,
    onUpvote: () -> Unit = {},
    onDownvote: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val tag = remember(id) { "post_$id" }
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

    var isHovered by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.01f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .testTag(tag),
        shape = RoundedCornerShape(14.dp),
        color = SurfaceDark.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, BorderGlow),
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isHovered = !isHovered
                    onClick()
                }
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    text = "👤",
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = content,
                    color = TextPrimary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Upvote
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StatusSuccess.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, StatusSuccess.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onUpvote,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Text("▲", color = StatusSuccess, fontSize = 14.sp)
                            }
                            Text(
                                text = upvoteCount.toString(),
                                color = StatusSuccess,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Downvote
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StatusError.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, StatusError.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onDownvote,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Text("▼", color = StatusError, fontSize = 14.sp)
                            }
                            Text(
                                text = downvoteCount.toString(),
                                color = StatusError,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Time badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = TextTertiary.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, TextTertiary.copy(alpha = 0.2f))
            ) {
                Text(
                    text = timeAgo,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun PostCardPreview() {
    val now = Instant.now()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
    ) {
        PostCard(
            id = 1,
            content = "Just completed a small refactor to improve the UI responsiveness. Here's a longer sample content to exercise the ellipsis truncation.",
            createdAt = now.minusSeconds(60 * 60),
            upvoteCount = 24,
            downvoteCount = 3
        )
    }
}
