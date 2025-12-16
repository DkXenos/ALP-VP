// kotlin
package com.jason.alp_vp.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.ui.model.Event
import com.jason.alp_vp.ui.viewmodel.ForumPageViewModel

// Colors (dark theme approximations)
private val Background = Color(0xFF0F1115)
private val CardBackground = Color(0xFF14161A)
private val AccentBlue = Color(0xFF2F6BFF)
private val TimeBadge = Color(0xFF413847)
private val RegisterGreen = Color(0xFF55D07E)
private val TitleColor = Color(0xFFFFFFFF)
private val SubText = Color(0xFF98A0B3)

// EventCard implementation moved to EventCard.kt
// Keputusan: fungsi EventCard dipindahkan ke file terpisah `EventCard.kt` untuk modularitas.

// Main screen with two sections - Event Posts and Community Feed
@Composable
fun ForumPage(
    viewModel: ForumPageViewModel = viewModel(),
    bountyViewModel: com.jason.alp_vp.ui.viewmodel.BountyViewModel = viewModel(),
    onNavigateToEventPage: () -> Unit = {},
    onNavigateToPostPage: () -> Unit = {},
    onNavigateToPostDetail: (Int) -> Unit = {},
    onNavigateToBountyDetail: (String) -> Unit = {}
) {
    val bounties by bountyViewModel.bounties.collectAsState()
    val isLoadingBounties by bountyViewModel.isLoading.collectAsState()
    val bountyError by bountyViewModel.error.collectAsState()

    Surface(color = Background, modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Bounties Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Available Bounties",
                        color = TitleColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { /* TODO: Navigate to all bounties */ }) {
                        Text(
                            text = "View All",
                            color = AccentBlue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Show bounties or loading/error state
            if (isLoadingBounties) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(CardBackground),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material3.CircularProgressIndicator(color = AccentBlue)
                        }
                    }
                }
            } else if (bountyError != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(CardBackground),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚠️ Failed to load bounties",
                                color = Color(0xFFF85C5C),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = bountyError ?: "Unknown error",
                                color = SubText,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { bountyViewModel.loadAllBounties() },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            } else if (bounties.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(CardBackground),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🎯", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No bounties available",
                                color = SubText,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            } else {
                // Show all bounties (not limited to 3)
                items(bounties) { bounty ->
                    BountyCardItem(
                        bounty = bounty,
                        onClaimClick = { bountyViewModel.claimBounty(bounty.id) },
                        onClick = { onNavigateToBountyDetail(bounty.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}


@Composable
fun BountyCardItem(
    bounty: com.jason.alp_vp.ui.model.Bounty,
    onClaimClick: () -> Unit,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(CardBackground),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bounty.title,
                        color = TitleColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = bounty.company,
                        color = SubText,
                        fontSize = 14.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (bounty.status.lowercase()) {
                        "active" -> Color(0xFF2F6BFF).copy(alpha = 0.15f)
                        "claimed" -> Color(0xFFFFA500).copy(alpha = 0.15f)
                        "completed" -> Color(0xFF55D07E).copy(alpha = 0.15f)
                        else -> Color(0xFF666666).copy(alpha = 0.15f)
                    }
                ) {
                    Text(
                        text = bounty.status.uppercase(),
                        color = when (bounty.status.lowercase()) {
                            "active" -> Color(0xFF2F6BFF)
                            "claimed" -> Color(0xFFFFA500)
                            "completed" -> Color(0xFF55D07E)
                            else -> Color(0xFF666666)
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (bounty.rewardMoney > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💰", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Rp ${java.text.NumberFormat.getNumberInstance(java.util.Locale("id", "ID")).format(bounty.rewardMoney)}",
                            color = RegisterGreen,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (bounty.status.lowercase() == "active") {
                    Button(
                        onClick = onClaimClick,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Claim", fontSize = 14.sp)
                    }
                }
            }

            if (bounty.deadline.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "⏰ Deadline: ${bounty.deadline.take(10)}",
                    color = SubText,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForumPagePreview() {
    ForumPage()
}
