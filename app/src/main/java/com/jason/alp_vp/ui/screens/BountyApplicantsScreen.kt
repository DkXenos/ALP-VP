package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.jason.alp_vp.data.service.Applicant
import com.jason.alp_vp.ui.viewmodel.BountyDetailViewModel
import java.text.SimpleDateFormat
import java.util.*

private val Background = Color(0xFF0F1115)
private val CardBackground = Color(0xFF14161A)
private val AccentBlue = Color(0xFF2B62FF)
private val AccentGreen = Color(0xFF57D06A)
private val AccentGold = Color(0xFFFFD700)
private val AccentRed = Color(0xFFF85C5C)
private val TitleColor = Color(0xFFFFFFFF)
private val SubText = Color(0xFF98A0B3)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BountyApplicantsScreen(
    bountyId: String,
    onNavigateBack: () -> Unit,
    viewModel: BountyDetailViewModel = viewModel()
) {
    val bountyDetail by viewModel.bountyDetail.collectAsState()
    val applicants by viewModel.applicants.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val winnerSelected by viewModel.winnerSelected.collectAsState()

    var showSelectWinnerDialog by remember { mutableStateOf(false) }
    var selectedApplicant by remember { mutableStateOf<Applicant?>(null) }

    LaunchedEffect(bountyId) {
        viewModel.loadBountyDetail(bountyId)
        viewModel.loadApplicants(bountyId)
    }

    LaunchedEffect(winnerSelected) {
        if (winnerSelected) {
            showSelectWinnerDialog = false
            viewModel.resetWinnerSelected()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bounty Applicants", color = TitleColor) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TitleColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CardBackground
                )
            )
        },
        containerColor = Background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading && applicants.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = AccentBlue
                    )
                }
                error != null && applicants.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "⚠️ Failed to Load Applicants",
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
                            onClick = { viewModel.loadApplicants(bountyId) },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                        ) {
                            Text("Retry")
                        }
                    }
                }
                applicants.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = SubText,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Applicants Yet",
                            color = TitleColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No one has claimed this bounty yet",
                            color = SubText,
                            fontSize = 14.sp
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Bounty Info Card
                        bountyDetail?.let { bounty ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = CardBackground),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = bounty.title,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TitleColor
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row {
                                        Text(
                                            text = "Rewards: ",
                                            fontSize = 14.sp,
                                            color = SubText
                                        )
                                        Text(
                                            text = "Rp ${bounty.rewardMoney ?: 0}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = AccentGreen
                                        )
                                        Text(
                                            text = " • ",
                                            fontSize = 14.sp,
                                            color = SubText
                                        )
                                        Text(
                                            text = "${bounty.rewardXp ?: 0} XP",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = AccentBlue
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Applicants Count
                        Text(
                            text = "${applicants.size} Applicant${if (applicants.size != 1) "s" else ""}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TitleColor
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Applicants List
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(applicants) { applicant ->
                                ApplicantCard(
                                    applicant = applicant,
                                    onSelectWinner = {
                                        selectedApplicant = applicant
                                        showSelectWinnerDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Select Winner Confirmation Dialog
    if (showSelectWinnerDialog && selectedApplicant != null) {
        AlertDialog(
            onDismissRequest = { showSelectWinnerDialog = false },
            containerColor = CardBackground,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = AccentGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Select Winner", color = TitleColor, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        "Are you sure you want to select ${selectedApplicant!!.username ?: selectedApplicant!!.email} as the winner?",
                        color = SubText
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "This action will:",
                        color = TitleColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "• Award ${bountyDetail?.rewardXp ?: 0} XP to the winner",
                        color = SubText,
                        fontSize = 13.sp
                    )
                    Text(
                        "• Transfer Rp ${bountyDetail?.rewardMoney ?: 0} to their balance",
                        color = SubText,
                        fontSize = 13.sp
                    )
                    Text(
                        "• Mark the bounty as COMPLETED",
                        color = SubText,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "This action cannot be undone.",
                        color = AccentRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.selectWinner(bountyId, selectedApplicant!!.userId)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Select Winner", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSelectWinnerDialog = false }) {
                    Text("Cancel", color = SubText)
                }
            }
        )
    }
}

@Composable
private fun ApplicantCard(
    applicant: Applicant,
    onSelectWinner: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (applicant.isWinner) AccentGold.copy(alpha = 0.1f) else CardBackground
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with user info and winner badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = if (applicant.isWinner) AccentGold else AccentBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = applicant.username ?: "User #${applicant.userId}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TitleColor
                        )
                        Text(
                            text = applicant.email,
                            fontSize = 12.sp,
                            color = SubText
                        )
                    }
                }

                if (applicant.isWinner) {
                    Surface(
                        color = AccentGold.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = AccentGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "WINNER",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentGold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Claimed date
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    tint = SubText,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Claimed: ${formatDate(applicant.claimedAt)}",
                    fontSize = 12.sp,
                    color = SubText
                )
            }

            // Submission info
            if (!applicant.submissionUrl.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = SubText.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AccentGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Work Submitted",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AccentGreen
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Submission URL:",
                    fontSize = 11.sp,
                    color = SubText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = applicant.submissionUrl,
                    fontSize = 13.sp,
                    color = AccentBlue,
                    fontWeight = FontWeight.Medium
                )

                if (!applicant.submissionNotes.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Notes:",
                        fontSize = 11.sp,
                        color = SubText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = applicant.submissionNotes,
                        fontSize = 13.sp,
                        color = TitleColor
                    )
                }

                if (!applicant.submittedAt.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Submitted: ${formatDate(applicant.submittedAt)}",
                        fontSize = 11.sp,
                        color = SubText
                    )
                }

                // Select Winner Button
                if (!applicant.isWinner) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onSelectWinner,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Select as Winner", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = SubText,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Work not submitted yet",
                        fontSize = 13.sp,
                        color = SubText,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
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

