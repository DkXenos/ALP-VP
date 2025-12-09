package com.jason.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

@Suppress("unused")
@Composable
fun EventForumCard(post: Post, modifier: Modifier = Modifier) {
    // compute a simple time-ago string from createdAt
    val hoursAgo = ((System.currentTimeMillis() - post.createdAt) / 3_600_000).coerceAtLeast(0)
    val timeAgoText = if (hoursAgo <= 0) "now" else "${hoursAgo}h ago"
    val authorText = "User"

    Card(
        colors = CardDefaults.cardColors(Color(0xFF14161A)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2B62FF))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text(authorText, color = Color(0xFFFFFFFF), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(timeAgoText, color = Color(0xFF98A0B3), fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(post.content, color = Color(0xFF98A0B3), maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}