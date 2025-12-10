// kotlin
package com.jason.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jason.alp_vp.ui.viewmodel.ForumPageViewModel
import java.time.Duration

// Colors (dark theme approximations)
private val Background = Color(0xFF0F1115)
private val CardBackground = Color(0xFF14161A)
private val AccentBlue = Color(0xFF2F6BFF)
private val TimeBadge = Color(0xFF413847)
private val RegisterGreen = Color(0xFF55D07E)
private val TitleColor = Color(0xFFFFFFFF)
private val SubText = Color(0xFF98A0B3)

// small helper to format Duration like "2d 13h" or "5h 29m"
private fun formatDurationShort(d: Duration?): String {
    if (d == null) return ""
    val totalMinutes = d.toMinutes()
    val days = totalMinutes / (60 * 24)
    val hours = (totalMinutes % (60 * 24)) / 60
    val minutes = totalMinutes % 60
    return when {
        days > 0 -> "${days}d ${hours}h"
        hours > 0 -> "${hours}h ${minutes}m"
        else -> "${minutes}m"
    }
}

// Event card composable (unchanged)
@Composable
fun EventCard(
    event: com.jason.alp_vp.ui.model.EventPost,
    modifier: Modifier = Modifier,
    onRegister: (String) -> Unit = {}
) {
    val timeText = formatDurationShort(event.timeRemaining)
    Card (
        colors = CardDefaults.cardColors(CardBackground),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    // Left tag
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color(0xFF0F2B7F), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text("EVENT", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Time badge top-right
                    Box(
                        modifier = Modifier
                            .background(TimeBadge, shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(timeText, color = Color(0xFFFFE9E9), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title
                Text(
                    text = event.title,
                    color = TitleColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Byline
                Text(text = "by ${event.organizer}", color = SubText, fontSize = 13.sp)

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = event.description,
                    color = SubText,
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "${event.registered}/${event.capacity} registered",
                        color = SubText,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button (
                        onClick = { onRegister(event.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = RegisterGreen),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Text("Register Now", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// Main screen with tabs - wired to ForumPageViewModel
@Composable
fun ForumPage(
    showOnlyFollowed: Boolean = false,
    viewModelProvided: ForumPageViewModel? = null
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Events, 1 = Forums

    // If lifecycle ViewModel provider is available, caller can pass it; otherwise create a remembered instance
    val viewModel = viewModelProvided ?: remember { ForumPageViewModel() }

    val eventPosts by viewModel.eventPosts.collectAsState()
    val postUis by viewModel.postUis.collectAsState()

    Surface (color = Background, modifier = Modifier.fillMaxSize()) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            // Header
            Text(
                text = if (selectedTab == 0) "Event Posts" else "Community Feed",
                color = TitleColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Tabs
            TabRow (
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = AccentBlue,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Tab (selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                    Text("Events", modifier = Modifier.padding(12.dp))
                }
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                    Text("Forums", modifier = Modifier.padding(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Content list
            val visibleEvents = if (showOnlyFollowed) eventPosts.filter { it.registered > 0 } else eventPosts
            val visiblePostUis = if (showOnlyFollowed) postUis.filter { it.upvoteCount >= it.downvoteCount } else postUis

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
                if (selectedTab == 0) {
                    items(visibleEvents) { ev ->
                        EventCard(ev, onRegister = { viewModel.registerToEvent(it) })
                    }
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                    item {
                        Text(
                            text = "Community Feed",
                            color = TitleColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                        )
                    }
                    items(postUis) { postUi ->
                        PostCard(
                            id = postUi.post.id,
                            content = postUi.post.content,
                            createdAt = postUi.post.createdAt,
                            upvoteCount = postUi.upvoteCount,
                            downvoteCount = postUi.downvoteCount,
                            onUpvote = { viewModel.upvote(postUi.post.id) },
                            onDownvote = { viewModel.downvote(postUi.post.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(40.dp)) }
                } else {
                    items(visiblePostUis) { postUi ->
                        // Map PostUi to primitive params for the reusable PostCard
                        val post = postUi.post
                        PostCard(
                            id = post.id,
                            content = post.content,
                            createdAt = post.createdAt,
                            upvoteCount = postUi.upvoteCount,
                            downvoteCount = postUi.downvoteCount,
                            onUpvote = { viewModel.upvote(post.id) },
                            onDownvote = { viewModel.downvote(post.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForumPagePreview() {
    ForumPage()
}
