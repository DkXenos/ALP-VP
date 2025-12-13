package com.jason.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jason.alp_vp.data.DummyDataRepository
import com.jason.alp_vp.ui.model.Bounty
import java.text.NumberFormat
import java.util.Locale

// Dark theme colors
private val BackgroundDark = Color(0xFF0A0E27)
private val CardBackgroundDark = Color(0xFF1A1F3A)
private val AccentBlue = Color(0xFF4A90E2)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFB0B8C4)
private val GreenReward = Color(0xFF4CAF50)
private val ButtonBlue = Color(0xFF2962FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BountyDetailScreen(
    bountyId: String,
    onBackClick: () -> Unit = {},
    onApplyClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Get bounty data from repository (will be replaced with ViewModel/Repository pattern)
    val bounty = remember(bountyId) {
        val baseBounty = DummyDataRepository.getBountyById(bountyId)
        val details = DummyDataRepository.getBountyDetails(bountyId)
        baseBounty?.copy(
            description = details?.description,
            requirements = details?.requirements
        )
    }

    if (bounty == null) {
        // Show error state
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark),
            contentAlignment = Alignment.Center
        ) {
            Text("Bounty not found", color = TextPrimary)
        }
        return
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            TopAppBar(
                title = {
                    Text(
                        text = "Bounty Details",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CardBackgroundDark
                )
            )

            // Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                // Bounty Title and Company
                item {
                    Column {
                        Text(
                            text = bounty.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "by ${bounty.company}",
                            fontSize = 16.sp,
                            color = AccentBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Reward Card
                item {
                    RewardCard(bounty = bounty)
                }

                // Description
                bounty.description?.let { description ->
                    item {
                        SectionCard(
                            title = "Description",
                            content = description
                        )
                    }
                }

                // Requirements
                bounty.requirements?.let { requirements ->
                    item {
                        RequirementsCard(requirements = requirements)
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }

            // Apply Button at bottom
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardBackgroundDark,
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = { onApplyClick(bountyId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Apply Now",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RewardCard(bounty: Bounty) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Reward",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // XP Badge
                Column {
                    Text(
                        text = "XP Points",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = AccentBlue.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "${bounty.rewardXp} XP",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentBlue,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }

                // Money
                Column {
                    Text(
                        text = "Cash Prize",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = formatCurrency(bounty.rewardMoney),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenReward
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = TextSecondary.copy(alpha = 0.2f))

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Deadline",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                Text(
                    text = bounty.deadline,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = content,
                fontSize = 14.sp,
                color = TextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun RequirementsCard(requirements: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Requirements",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            requirements.forEach { requirement ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "•  ",
                        fontSize = 14.sp,
                        color = AccentBlue
                    )
                    Text(
                        text = requirement,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

private fun formatCurrency(amount: Int): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
    return formatter.format(amount).replace("Rp", "Rp ")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BountyDetailScreenPreview() {
    BountyDetailScreen(
        bountyId = "1"
    )
}

