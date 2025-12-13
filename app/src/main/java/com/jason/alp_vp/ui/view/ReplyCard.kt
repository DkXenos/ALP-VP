package com.jason.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jason.alp_vp.ui.model.Post

@Composable
fun ReplyItem(reply: Post, authorInitial: String = "S", authorName: String = "User", onUpvote: () -> Unit = {}, onDownvote: () -> Unit = {}) {
    // Hitung votes dari comments
    val upvotes = 0
    val downvotes = 0
    // TODO: Implement vote counting from reply.comments
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14161A)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF1DB954)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(authorInitial, color = Color.White, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(authorName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "1h ago", color = Color(0xFF98A0B3), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = reply.content, color = Color(0xFFDDE1E6), fontSize = 14.sp, maxLines = 4, overflow = TextOverflow.Ellipsis)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button (
                            onClick = onUpvote,
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

                        Button (
                            onClick = onDownvote,
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
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun ReplyItemPreview() {
    val now = java.time.Instant.now()
    val sampleReply = com.jason.alp_vp.ui.model.Post(
        id = 2,
        userId = 10,
        content = "Nice work! Could you share the repo link?",
        image = null,
        createdAt = now,
        comments = emptyList()
    )

    ReplyItem(reply = sampleReply, authorInitial = "N", authorName = "Nina")
}
