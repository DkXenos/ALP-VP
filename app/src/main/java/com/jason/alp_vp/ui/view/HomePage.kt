package com.jason.alp_vp.ui.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.ui.theme.*
import com.jason.alp_vp.ui.viewmodel.BountyViewModel

@Composable
fun HomePage(
    bountyViewModel: BountyViewModel = viewModel(),
    onNavigateToBountyDetail: (String) -> Unit = {}
) {
    val bounties by bountyViewModel.bounties.collectAsState()
    val myBounties by bountyViewModel.myBounties.collectAsState()
    val isLoadingBounties by bountyViewModel.isLoading.collectAsState()
    val bountyError by bountyViewModel.error.collectAsState()

    // Load bounties when screen loads
    LaunchedEffect(Unit) {
        bountyViewModel.loadAllBounties()
        bountyViewModel.loadMyBounties()
    }

    Surface(color = BackgroundDark, modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            // My Bounties Section (if user has claimed bounties)
            if (myBounties.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "My Active Bounties",
                            color = TextPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = AccentCyan.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, AccentCyan.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = myBounties.size.toString(),
                                color = AccentCyan,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                items(myBounties) { bounty ->
                    BountyCardItem(
                        bounty = bounty,
                        isClaimed = true,
                        onClick = { onNavigateToBountyDetail(bounty.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Bounties Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Available Bounties",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    TextButton(onClick = { /* TODO: Navigate to all bounties */ }) {
                        Text(
                            text = "View All",
                            color = AccentCyan,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Show bounties or loading/error state
            if (isLoadingBounties) {
                item {
                    GlowingCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = AccentCyan,
                                strokeWidth = 3.dp
                            )
                        }
                    }
                }
            } else if (bountyError != null) {
                item {
                    GlowingCard(
                        glowColor = StatusError.copy(alpha = 0.3f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚠️ Failed to load bounties",
                                color = StatusError,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = bountyError ?: "Unknown error",
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            GradientButton(
                                text = "Retry",
                                onClick = { bountyViewModel.loadAllBounties() }
                            )
                        }
                    }
                }
            } else if (bounties.isEmpty()) {
                item {
                    GlowingCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🎯", fontSize = 52.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No bounties available",
                                color = TextSecondary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            } else {
                // Show all bounties
                items(bounties) { bounty ->
                    BountyCardItem(
                        bounty = bounty,
                        isClaimed = myBounties.any { it.id == bounty.id },
                        onClick = { onNavigateToBountyDetail(bounty.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
private fun BountyCardItem(
    bounty: com.jason.alp_vp.ui.model.Bounty,
    isClaimed: Boolean = false,
    onClick: () -> Unit = {}
) {
    var isHovered by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.02f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    GlowingCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .scale(scale)
            .clickable {
                isHovered = !isHovered
                onClick()
            },
        glowColor = if (isClaimed) AccentCyan.copy(alpha = 0.4f) else BorderGlow
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = bounty.title,
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (isClaimed) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = StatusWarning.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, StatusWarning.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "CLAIMED",
                                    color = StatusWarning,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.8.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = bounty.company,
                        color = AccentCyan,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                StatusPill(status = bounty.status)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rewards row with gradient backgrounds
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (bounty.rewardMoney > 0) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        StatusSuccess.copy(alpha = 0.15f),
                                        StatusSuccess.copy(alpha = 0.05f)
                                    )
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "💰", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Rp ${java.text.NumberFormat.getNumberInstance(java.util.Locale("id", "ID")).format(bounty.rewardMoney)}",
                                color = StatusSuccess,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (bounty.rewardXp > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        AccentCyan.copy(alpha = 0.15f),
                                        AccentCyan.copy(alpha = 0.05f)
                                    )
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "⭐", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${bounty.rewardXp} XP",
                                color = AccentCyan,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            if (bounty.deadline.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⏰", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Deadline: ${bounty.deadline.take(10)}",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusPill(status: String) {
    val (bgColor, textColor) = when (status.lowercase()) {
        "active", "open" -> Pair(AccentCyan.copy(alpha = 0.15f), AccentCyan)
        "claimed" -> Pair(StatusWarning.copy(alpha = 0.15f), StatusWarning)
        "completed" -> Pair(StatusSuccess.copy(alpha = 0.15f), StatusSuccess)
        "closed" -> Pair(TextTertiary.copy(alpha = 0.15f), TextTertiary)
        else -> Pair(TextTertiary.copy(alpha = 0.15f), TextTertiary)
    }

    val borderColor = when (status.lowercase()) {
        "active", "open" -> AccentCyan.copy(alpha = 0.3f)
        "claimed" -> StatusWarning.copy(alpha = 0.3f)
        "completed" -> StatusSuccess.copy(alpha = 0.3f)
        else -> TextTertiary.copy(alpha = 0.2f)
    }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = status.uppercase(),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

// Reusable glowing card component
@Composable
fun GlowingCard(
    modifier: Modifier = Modifier,
    glowColor: Color = BorderGlow,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = SurfaceDark.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, glowColor),
        shadowElevation = 8.dp
    ) {
        content()
    }
}

// Reusable gradient button
@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = TextTertiary.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (enabled) {
                        Brush.horizontalGradient(
                            colors = listOf(GradientStart, GradientMiddle, GradientEnd)
                        )
                    } else {
                        Brush.horizontalGradient(
                            colors = listOf(TextTertiary, TextTertiary)
                        )
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}
