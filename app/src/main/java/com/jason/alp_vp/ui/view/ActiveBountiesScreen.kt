package com.jason.alp_vp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.ui.theme.*
import com.jason.alp_vp.ui.viewmodel.ProfileViewModel

@Composable
fun ActiveBountiesScreen(
    profileViewModel: ProfileViewModel = viewModel(),
    onNavigateToBountyDetail: (String) -> Unit = {}
) {
    val profileData by profileViewModel.profileData.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()
    val error by profileViewModel.error.collectAsState()

    // Load profile data when screen loads
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    Surface(color = BackgroundDark, modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Active Bounties Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "My Active Bounties",
                            color = TextPrimary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Bounties you're currently working on",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }

                    Surface(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                        color = AccentCyan.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = (profileData?.bounties?.size ?: 0).toString(),
                            color = AccentCyan,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // Show loading, error, or bounties
            if (isLoading) {
                item {
                    GlowingCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = AccentCyan,
                                strokeWidth = 3.dp
                            )
                        }
                    }
                }
            } else if (error != null) {
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
                                .padding(30.dp),
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
                                text = error ?: "Unknown error",
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            GradientButton(
                                text = "Retry",
                                onClick = { profileViewModel.loadProfile() }
                            )
                        }
                    }
                }
            } else {
                profileData?.bounties?.let { bounties ->
                    if (bounties.isEmpty()) {
                        // Empty state - no active bounties
                        item {
                            GlowingCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(50.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "📋", fontSize = 72.sp)
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(
                                        text = "No Active Bounties",
                                        color = TextPrimary,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "You haven't claimed any bounties yet.\nGo to Home to find available bounties!",
                                        color = TextSecondary,
                                        fontSize = 15.sp,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }
                    } else {
                        // Show active bounties using BountyItemCard from ProfileScreen
                        items(bounties) { bounty ->
                            com.jason.alp_vp.ui.screens.BountyItemCard(
                                bounty = bounty
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

