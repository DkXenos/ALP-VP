package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.ui.model.Bounty
import com.jason.alp_vp.viewmodel.BountyUiState
import com.jason.alp_vp.viewmodel.HomeViewModel

// Cyberpunk Color Palette
private val NavyBlue = Color(0xFF0A1929)
private val DarkNavy = Color(0xFF071422)
private val NeonGreen = Color(0xFF00FF41)
private val NeonGreenDim = Color(0xFF00CC33)
private val CardBackground = Color(0xFF0F2234)
private val BorderNeon = Color(0xFF00FF41)

@Composable
fun BountiesPage(
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkNavy, NavyBlue)
                )
            )
    ) {
        when (val state = uiState) {
            is BountyUiState.Loading -> {
                LoadingScreen()
            }
            is BountyUiState.Success -> {
                BountiesContent(
                    bounties = state.bounties,
                    onRefresh = { viewModel.retry() }
                )
            }
            is BountyUiState.Error -> {
                ErrorScreen(
                    message = state.message,
                    onRetry = { viewModel.retry() }
                )
            }
        }
    }
}

@Composable
private fun BountiesContent(
    bounties: List<Bounty>,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "BOUNTIES",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = NeonGreen,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (bounties.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No bounties available",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(bounties) { bounty ->
                    BountyCard(bounty = bounty)
                }
            }
        }
    }
}

@Composable
fun BountyCard(bounty: Bounty) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BorderNeon.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = bounty.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NeonGreen,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Company
            Text(
                text = bounty.company,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Deadline
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Deadline: ",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Text(
                    text = bounty.deadline,
                    fontSize = 13.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Rewards and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rewards
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // XP
                        RewardChip(
                            label = "${bounty.rewardXp} XP",
                            backgroundColor = NeonGreen.copy(alpha = 0.2f),
                            textColor = NeonGreen
                        )
                        // Money
                        RewardChip(
                            label = "$${bounty.rewardMoney}",
                            backgroundColor = Color(0xFF4CAF50).copy(alpha = 0.2f),
                            textColor = Color(0xFF81C784)
                        )
                    }
                }

                // Status
                StatusChip(status = bounty.status)
            }
        }
    }
}

@Composable
private fun RewardChip(
    label: String,
    backgroundColor: Color,
    textColor: Color
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun StatusChip(status: String) {
    val (backgroundColor, textColor) = when (status.lowercase()) {
        "active" -> Pair(NeonGreen.copy(alpha = 0.2f), NeonGreen)
        "completed" -> Pair(Color(0xFF2196F3).copy(alpha = 0.2f), Color(0xFF64B5F6))
        "expired" -> Pair(Color(0xFFFF5252).copy(alpha = 0.2f), Color(0xFFFF8A80))
        else -> Pair(Color.Gray.copy(alpha = 0.2f), Color.LightGray)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        modifier = Modifier.border(
            width = 1.dp,
            color = textColor.copy(alpha = 0.5f),
            shape = RoundedCornerShape(8.dp)
        )
    ) {
        Text(
            text = status.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = NeonGreen,
                strokeWidth = 3.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading Bounties...",
                color = NeonGreen,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "⚠️",
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Error Loading Bounties",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF5252),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = message,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonGreen,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "RETRY",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
