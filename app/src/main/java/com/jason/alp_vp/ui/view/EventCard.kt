package com.jason.alp_vp.ui.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jason.alp_vp.ui.model.Event
import com.jason.alp_vp.ui.theme.*
import java.time.Duration
import java.time.Instant

@Composable
fun EventCard(
    event: Event,
    modifier: Modifier = Modifier,
    onRegister: (Int) -> Unit = {}
) {
    val timeUntil = remember(event.eventDate) {
        val duration = Duration.between(Instant.now(), event.eventDate)
        when {
            duration.isNegative -> "Event ended"
            duration.toDays() > 0 -> "in ${duration.toDays()} days"
            duration.toHours() > 0 -> "in ${duration.toHours()} hours"
            duration.toMinutes() > 0 -> "in ${duration.toMinutes()} minutes"
            else -> "starting soon"
        }
    }

    val registrationProgress = remember(event.currentRegistrations, event.registeredQuota) {
        if (event.registeredQuota > 0) {
            event.currentRegistrations.toFloat() / event.registeredQuota.toFloat()
        } else {
            0f
        }
    }

    val isFull = event.currentRegistrations >= event.registeredQuota

    var isHovered by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.02f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    // Animated glow effect for event card
    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        shadowElevation = 12.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SurfaceDarkElevated.copy(alpha = 0.8f),
                            SurfaceDark.copy(alpha = 0.6f)
                        )
                    )
                )
        ) {
            // Gradient overlay at the top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AccentCyan.copy(alpha = glowAlpha * 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header with event info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Company name with icon
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(AccentCyan, AccentPurple)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = event.companyName.take(1).uppercase(),
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = event.companyName,
                                color = AccentCyan,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Event title
                        Text(
                            text = event.title,
                            color = TextPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 28.sp
                        )
                    }

                    // Time indicator with glow
                    Surface(
                        color = StatusWarning.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, StatusWarning.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⏱️", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = timeUntil,
                                color = StatusWarning,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = event.description,
                    color = TextSecondary,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    maxLines = 3,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Registration progress with glowing bar
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Registration Progress",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${event.currentRegistrations} / ${event.registeredQuota}",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // Glowing progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(SurfaceDark)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(registrationProgress)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(5.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = if (isFull) {
                                            listOf(StatusWarning, StatusError)
                                        } else {
                                            listOf(StatusSuccess, AccentCyan)
                                        }
                                    )
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Register button with gradient
                Button(
                    onClick = { onRegister(event.id) },
                    enabled = !isFull,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = TextTertiary.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp),
                    border = if (!isFull) BorderStroke(1.dp, AccentCyan.copy(alpha = 0.3f)) else null
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                if (!isFull) {
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isFull) "Event Full" else "Register Now",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = TextPrimary
                            )
                            if (!isFull) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "→", fontSize = 18.sp, color = TextPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}
