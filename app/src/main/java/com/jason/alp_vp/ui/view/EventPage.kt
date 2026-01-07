package com.jason.alp_vp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
fun EventPage(
    viewModel: ForumPageViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    val events by viewModel.events.collectAsState()

    Surface(color = Background, modifier = Modifier.fillMaxSize()) {
        Column {
            // Header with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFFFFFFFF),
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "All Events",
                    color = TitleColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // List of all events
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(events) { event ->
                    EventCard(
                        event = event,
                        onRegister = { viewModel.registerToEvent(it) }
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
fun EventPagePreview() {
    EventPage()
}

