// kotlin
package com.jason.alp_vp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.ui.viewmodel.ForumPageViewModel

// Colors (dark theme)
private val Background = Color(0xFF0F1115)
private val TitleColor = Color(0xFFFFFFFF)
private val AccentBlue = Color(0xFF2F6BFF)

// ForumPage - Shows Event Posts and Community Feed (your friend's logic)
@Composable
fun ForumPage(
    viewModel: ForumPageViewModel = viewModel(),
    onNavigateToEventPage: () -> Unit = {},
    onNavigateToPostPage: () -> Unit = {},
    onNavigateToPostDetail: (Int) -> Unit = {}
) {
    val events by viewModel.events.collectAsState()
    val postUis by viewModel.postUis.collectAsState()
    val error by viewModel.error.collectAsState()

    Surface(color = Background, modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Show error banner if present
            item {
                if (!error.isNullOrBlank()) {
                    Text(
                        text = error ?: "",
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Event Posts Section Header
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
                            text = "View All",
                            color = AccentBlue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Show only first 2 events
            items(events.take(2)) { event ->
                EventCard(
                    event = event,
                    viewModel = viewModel,
                    onRegister = { viewModel.registerToEvent(it) }
                )
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
                            text = "View All",
                            color = AccentBlue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Show only first 3 posts
            items(postUis.take(3)) { postUi ->
                PostCard(
                    id = postUi.post.id,
                    content = postUi.post.content,
                    createdAt = postUi.post.createdAt,
                    upvoteCount = postUi.upvoteCount,
                    downvoteCount = postUi.downvoteCount,
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
}

@Preview(showBackground = true)
@Composable
fun ForumPagePreview() {
    ForumPage()
}
