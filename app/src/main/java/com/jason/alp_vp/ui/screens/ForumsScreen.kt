package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.model.ForumPost
import com.jason.alp_vp.ui.components.EventPost
import com.jason.alp_vp.ui.components.EventPostCard
import com.jason.alp_vp.ui.components.ForumPostCard
import com.jason.alp_vp.viewmodel.ForumViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumsScreen(
    onNavigateBack: () -> Unit,
    onPostClick: (String) -> Unit = {},
    onEventRegister: (String) -> Unit = {},
    viewModel: ForumViewModel = viewModel()
) {
    val posts by viewModel.posts.collectAsState()
    val eventPosts = getEventPosts() // Get event posts

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Forums") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Event Posts",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(eventPosts) { event ->
                EventPostCard(
                    event = event,
                    onRegister = { onEventRegister(event.id) }
                )
            }

            item {
                Text(
                    text = "Community Feed",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(posts) { post ->
                ForumPostCard(
                    post = post,
                    onClick = { onPostClick(post.id) }
                )
            }
        }
    }
}


fun getEventPosts(): List<EventPost> {
    val now = System.currentTimeMillis()
    return listOf(
        EventPost(
            id = "ev1",
            title = "🎯 Tech Startup Hiring Event",
            description = "Join us for an exclusive hiring event! We're looking for talented developers, designers, and marketers. Register now for priority access to our bounties!",
            companyName = "InnovateTech Corp",
            deadline = "Dec 10, 2024",
            participants = 45,
            maxParticipants = 100,
            endTimeMillis = now + (2 * 24 * 60 * 60 * 1000) + (14 * 60 * 60 * 1000) // 2 days 14 hours from now
        ),
        EventPost(
            id = "ev2",
            title = "🚀 Freelancer Networking Session",
            description = "Connect with other freelancers, share tips, and learn about upcoming opportunities. Special guest speakers from top companies!",
            companyName = "SideQuest Community",
            deadline = "Dec 8, 2024",
            participants = 78,
            maxParticipants = 150,
            endTimeMillis = now + (5 * 60 * 60 * 1000) + (30 * 60 * 1000) // 5 hours 30 minutes from now
        )
    )
}
