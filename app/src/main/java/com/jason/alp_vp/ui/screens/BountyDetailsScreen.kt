package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.model.UserRole
import com.jason.alp_vp.repository.MockRepository
import com.jason.alp_vp.ui.components.formatRupiah
import com.jason.alp_vp.viewmodel.BountyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BountyDetailsScreen(
    bountyId: String,
    onNavigateBack: () -> Unit,
    onApplyClick: (String) -> Unit,
    onViewApplicantsClick: (String) -> Unit,
    viewModel: BountyViewModel = viewModel()
) {
    val currentUser by MockRepository.currentUser.collectAsState()

    LaunchedEffect(bountyId) {
        viewModel.selectBounty(bountyId)
    }
    
    val bounty by viewModel.selectedBounty.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bounty Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        bounty?.let { currentBounty ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Title
                    Text(
                        text = currentBounty.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                
                item {
                    // Company
                    Text(
                        text = "by ${currentBounty.companyName}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }

                item {
                    // Reward Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Reward",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = formatRupiah(currentBounty.price),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }

                            Column {
                                Text(
                                    text = "XP Reward",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "+${currentBounty.xp} XP",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Column {
                                Text(
                                    text = "Recommended Level",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "Lvl ${currentBounty.minLevel}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                item {
                    // Description
                    Text(
                        text = "Description",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentBounty.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }

                item {
                    // Requirements
                    Text(
                        text = "Requirements",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                items(currentBounty.requirements) { requirement ->
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(
                            text = "• ",
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = requirement,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                    }
                }

                item {
                    // Deadline
                    Text(
                        text = "Deadline: ${currentBounty.deadline}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    // Action Button based on user role
                    when (currentUser?.role) {
                        UserRole.TALENT -> {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                // Show warning if user level is below recommended
                                val isUnderLevel = (currentUser?.level ?: 0) < currentBounty.minLevel

                                if (isUnderLevel) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 8.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "⚠️",
                                                fontSize = 16.sp,
                                                modifier = Modifier.padding(end = 8.dp)
                                            )
                                            Column {
                                                Text(
                                                    text = "Below Recommended Level",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                                Text(
                                                    text = "Your level: ${currentUser?.level ?: 0} | Recommended: ${currentBounty.minLevel}",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                                )
                                            }
                                        }
                                    }
                                }

                                Button(
                                    onClick = { onApplyClick(bountyId) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = if (isUnderLevel) {
                                        ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.tertiary
                                        )
                                    } else {
                                        ButtonDefaults.buttonColors()
                                    }
                                ) {
                                    Text(
                                        text = "Apply Now",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        UserRole.COMPANY -> {
                            Button(
                                onClick = { onViewApplicantsClick(bountyId) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "View Applicants",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        null -> {
                            // Not logged in
                        }
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

