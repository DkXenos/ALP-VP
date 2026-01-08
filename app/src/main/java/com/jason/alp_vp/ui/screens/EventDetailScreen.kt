package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.ui.viewmodel.EventDetailViewModel
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*

private val Background = Color(0xFF0F1115)
private val CardBackground = Color(0xFF14161A)
private val AccentBlue = Color(0xFF2B62FF)
private val AccentGreen = Color(0xFF57D06A)
private val AccentRed = Color(0xFFF85C5C)
private val AccentOrange = Color(0xFFFF9800)
private val AccentCyan = Color(0xFF00D9FF)
private val TitleColor = Color(0xFFFFFFFF)
private val SubText = Color(0xFF98A0B3)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: Int,
    onNavigateBack: () -> Unit,
    onEventRegistered: () -> Unit = {},
    viewModel: EventDetailViewModel = viewModel()
) {
    val eventDetail by viewModel.eventDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val registerSuccess by viewModel.registerSuccess.collectAsState()

    var showRegisterDialog by remember { mutableStateOf(false) }

    LaunchedEffect(eventId) {
        viewModel.loadEventDetail(eventId)
    }

    LaunchedEffect(registerSuccess) {
        if (registerSuccess) {
            showRegisterDialog = false
            viewModel.resetRegisterSuccess()
            onEventRegistered()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        when {
            isLoading && eventDetail == null -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AccentBlue
                )
            }
            error != null && eventDetail == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "⚠️ Failed to Load Event",
                        color = AccentRed,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error ?: "Unknown error",
                        color = SubText,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.loadEventDetail(eventId) },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                    ) {
                        Text("Retry")
                    }
                }
            }
            eventDetail != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Status Badge
                    EventStatusBadge(event = eventDetail!!)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title
                    Text(
                        text = eventDetail!!.title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TitleColor
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Company
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Business,
                            contentDescription = null,
                            tint = SubText,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = eventDetail!!.companyName,
                            fontSize = 16.sp,
                            color = SubText
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Event Date Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = null,
                                tint = AccentOrange,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Event Date",
                                    fontSize = 14.sp,
                                    color = SubText
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = formatDate(eventDetail!!.eventDate),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TitleColor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Registration Progress Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Registration Status",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TitleColor
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "👥 Registered",
                                        fontSize = 14.sp,
                                        color = SubText
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${eventDetail!!.currentRegistrations} / ${eventDetail!!.registeredQuota}",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentCyan
                                    )
                                }

                                // Progress Circle or Badge
                                val progress = if (eventDetail!!.registeredQuota > 0) {
                                    eventDetail!!.currentRegistrations.toFloat() / eventDetail!!.registeredQuota.toFloat()
                                } else {
                                    0f
                                }

                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.size(80.dp)
                                ) {
                                    CircularProgressIndicator(
                                        progress = progress,
                                        modifier = Modifier.fillMaxSize(),
                                        color = if (progress >= 1f) AccentRed else AccentGreen,
                                        strokeWidth = 8.dp,
                                        trackColor = SubText.copy(alpha = 0.2f)
                                    )
                                    Text(
                                        text = "${(progress * 100).toInt()}%",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TitleColor
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    if (eventDetail!!.description.isNotBlank()) {
                        InfoSection(
                            title = "Description",
                            content = eventDetail!!.description
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Spacer(modifier = Modifier.height(80.dp)) // Space for button
                }

                // Bottom Action Button
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    val isFull = eventDetail!!.currentRegistrations >= eventDetail!!.registeredQuota
                    val isPast = eventDetail!!.eventDate.isBefore(Instant.now())

                    when {
                        isPast -> {
                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Gray,
                                    disabledContainerColor = Color.Gray
                                ),
                                enabled = false,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Event Ended", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        isFull -> {
                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentRed.copy(alpha = 0.5f),
                                    disabledContainerColor = AccentRed.copy(alpha = 0.5f)
                                ),
                                enabled = false,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Event Full", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        else -> {
                            Button(
                                onClick = { showRegisterDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentGreen
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Register for Event", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    // Register Confirmation Dialog
    if (showRegisterDialog) {
        AlertDialog(
            onDismissRequest = { showRegisterDialog = false },
            containerColor = CardBackground,
            title = {
                Text("Register for Event", color = TitleColor, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Are you sure you want to register for this event? You'll receive updates and details about the event.",
                    color = SubText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.registerToEvent(eventId)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                ) {
                    Text("Register", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRegisterDialog = false }) {
                    Text("Cancel", color = SubText)
                }
            }
        )
    }
}

@Composable
private fun EventStatusBadge(event: com.jason.alp_vp.ui.model.Event) {
    val isPast = event.eventDate.isBefore(Instant.now())
    val isFull = event.currentRegistrations >= event.registeredQuota
    val timeUntil = Duration.between(Instant.now(), event.eventDate)

    val (statusText, statusColor) = when {
        isPast -> "Ended" to AccentRed
        isFull -> "Full" to AccentOrange
        timeUntil.toDays() <= 7 -> "Coming Soon" to AccentGreen
        else -> "Open" to AccentCyan
    }

    Surface(
        color = statusColor.copy(alpha = 0.15f),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.4f))
    ) {
        Text(
            text = statusText,
            color = statusColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun InfoSection(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TitleColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = content,
                fontSize = 15.sp,
                color = SubText,
                lineHeight = 22.sp
            )
        }
    }
}

private fun formatDate(instant: Instant): String {
    val formatter = SimpleDateFormat("EEEE, MMMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
    return formatter.format(Date.from(instant))
}

