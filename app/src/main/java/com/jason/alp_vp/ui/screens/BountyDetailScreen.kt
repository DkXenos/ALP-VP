package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.jason.alp_vp.ui.viewmodel.BountyDetailViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private val Background = Color(0xFF0F1115)
private val CardBackground = Color(0xFF14161A)
private val AccentBlue = Color(0xFF2B62FF)
private val AccentGreen = Color(0xFF57D06A)
private val AccentRed = Color(0xFFF85C5C)
private val AccentOrange = Color(0xFFFF9800)
private val TitleColor = Color(0xFFFFFFFF)
private val SubText = Color(0xFF98A0B3)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BountyDetailScreen(
    bountyId: String,
    onNavigateBack: () -> Unit,
    onBountyClaimed: () -> Unit = {},
    viewModel: BountyDetailViewModel = viewModel()
) {
    val bountyDetail by viewModel.bountyDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val claimSuccess by viewModel.claimSuccess.collectAsState()

    var showClaimDialog by remember { mutableStateOf(false) }

    LaunchedEffect(bountyId) {
        viewModel.loadBountyDetail(bountyId)
    }

    LaunchedEffect(claimSuccess) {
        if (claimSuccess) {
            showClaimDialog = false
            viewModel.resetClaimSuccess()
            // Notify parent that bounty was claimed
            onBountyClaimed()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        when {
            isLoading && bountyDetail == null -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AccentBlue
                    )
                }
                error != null && bountyDetail == null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "⚠️ Failed to Load Bounty",
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
                            onClick = { viewModel.loadBountyDetail(bountyId) },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                        ) {
                            Text("Retry")
                        }
                    }
                }
                bountyDetail != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // Status Badge
                        StatusBadge(status = bountyDetail!!.status)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Title
                        Text(
                            text = bountyDetail!!.title,
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
                                text = bountyDetail!!.company ?: bountyDetail!!.companyName ?: "Unknown Company",
                                fontSize = 16.sp,
                                color = SubText
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Rewards Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "Rewards",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TitleColor
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Money Reward
                                    Column {
                                        Text(
                                            text = "💰 Money",
                                            fontSize = 14.sp,
                                            color = SubText
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = formatRupiah(bountyDetail!!.rewardMoney ?: 0),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AccentGreen
                                        )
                                    }

                                    // XP Reward
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "⭐ Experience",
                                            fontSize = 14.sp,
                                            color = SubText
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${bountyDetail!!.rewardXp ?: 0} XP",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AccentBlue
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Deadline Card
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
                                        text = "Deadline",
                                        fontSize = 14.sp,
                                        color = SubText
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = formatDate(bountyDetail!!.deadline ?: ""),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TitleColor
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Description
                        if (!bountyDetail!!.description.isNullOrBlank()) {
                            InfoSection(
                                title = "Description",
                                content = bountyDetail!!.description!!
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
                        when {
                            bountyDetail!!.claimedBy != null -> {
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
                                    Icon(Icons.Default.Check, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Already Claimed", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            bountyDetail!!.status.uppercase() == "CLOSED" -> {
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
                                    Text("Bounty Closed", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            else -> {
                                Button(
                                    onClick = { showClaimDialog = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AccentGreen
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Claim Bounty", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

    // Claim Confirmation Dialog
    if (showClaimDialog) {
        AlertDialog(
            onDismissRequest = { showClaimDialog = false },
            containerColor = CardBackground,
            title = {
                Text("Claim Bounty", color = TitleColor, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Are you sure you want to claim this bounty? You'll be responsible for completing it by the deadline.",
                    color = SubText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.claimBounty(bountyId)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                ) {
                    Text("Claim", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClaimDialog = false }) {
                    Text("Cancel", color = SubText)
                }
            }
        )
    }
}

@Composable
private fun StatusBadge(status: String) {
    val (color, text) = when (status.uppercase()) {
        "OPEN" -> AccentGreen to "OPEN"
        "CLOSED" -> AccentRed to "CLOSED"
        "COMPLETED" -> AccentBlue to "COMPLETED"
        else -> Color.Gray to status.uppercase()
    }

    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = color
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
                fontSize = 14.sp,
                color = SubText,
                lineHeight = 20.sp
            )
        }
    }
}

private fun formatRupiah(amount: Int): String {
    val format = NumberFormat.getNumberInstance(Locale("id", "ID"))
    return "Rp ${format.format(amount)}"
}

private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(dateString)
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        date?.let { outputFormat.format(it) } ?: dateString.take(10)
    } catch (e: Exception) {
        dateString.take(10)
    }
}

