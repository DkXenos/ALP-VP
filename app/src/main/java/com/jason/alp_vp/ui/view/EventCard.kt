package com.jason.alp_vp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jason.alp_vp.ui.model.Event
import java.time.Duration
import java.time.Instant

// Colors (dark theme)
private val CardBackground = Color(0xFF14161A)
private val TitleColor = Color(0xFFFFFFFF)
private val SubText = Color(0xFF98A0B3)
private val AccentBlue = Color(0xFF2F6BFF)
private val AccentGreen = Color(0xFF57D06A)
private val AccentOrange = Color(0xFFFF9500)

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

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event.title,
                        color = TitleColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = event.companyName,
                        color = AccentBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Time indicator
                Surface(
                    color = AccentOrange.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = timeUntil,
                        color = AccentOrange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = event.description,
                color = SubText,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                maxLines = 3,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Registration progress
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Registrations",
                        color = SubText,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "${event.currentRegistrations} / ${event.registeredQuota}",
                        color = TitleColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { registrationProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = if (isFull) AccentOrange else AccentGreen,
                    trackColor = Color(0xFF2A2D35)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Register button
            Button(
                onClick = { onRegister(event.id) },
                enabled = !isFull,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBlue,
                    disabledContainerColor = Color(0xFF2A2D35)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (isFull) "Event Full" else "Register Now",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

