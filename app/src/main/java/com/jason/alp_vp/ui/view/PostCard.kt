// kotlin
package com.jason.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
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
    val hoursAgo = Duration.between(createdAt, Instant.now()).toHours()

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14161A)),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag(tag)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFF2B62FF))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = content, color = Color.White, maxLines = 3, overflow = TextOverflow.Ellipsis, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onUpvote) {
                        Text("▲", color = Color(0xFF57D06A), fontSize = 16.sp)
                    }
                    Text(text = upvoteCount.toString(), color = Color(0xFF98A0B3), fontSize = 13.sp)

                    Spacer(modifier = Modifier.width(12.dp))

                    IconButton(onClick = onDownvote) {
                        Text("▼", color = Color(0xFFF85C5C), fontSize = 16.sp)
                    }
                    Text(text = downvoteCount.toString(), color = Color(0xFF98A0B3), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "${hoursAgo}h", color = Color(0xFF98A0B3), fontSize = 12.sp)
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun PostCardPreview() {
    val now = java.time.Instant.now()
    PostCard(
        id = 1,
        content = "Just completed a small refactor to improve the UI responsiveness. Here's a longer sample content to exercise the ellipsis truncation.",
        createdAt = now.minusSeconds(60 * 60),
        upvoteCount = 24,
        downvoteCount = 3
    )
}
