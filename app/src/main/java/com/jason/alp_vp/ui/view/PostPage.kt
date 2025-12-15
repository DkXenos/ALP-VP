package com.jason.alp_vp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
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

// Colors
private val Background = Color(0xFF0F1115)
private val TitleColor = Color(0xFFFFFFFF)

@Composable
fun PostPage(
    viewModel: ForumPageViewModel = viewModel(),
    onBack: () -> Unit = {},
    onNavigateToPostDetail: (Int) -> Unit = {}
) {
    val postUis by viewModel.postUis.collectAsState()

    Surface(color = Background, modifier = Modifier.fillMaxSize()) {
        Column {
            // Header with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Text("←", color = TitleColor, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Community Feed",
                    color = TitleColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // List of all posts
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(postUis) { postUi ->
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
}

@Preview(showBackground = true)
@Composable
fun PostPagePreview() {
    PostPage()
}

