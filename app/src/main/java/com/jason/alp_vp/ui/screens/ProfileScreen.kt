package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.jason.alp_vp.data.service.*
import com.jason.alp_vp.ui.viewmodel.ProfileViewModel
import com.jason.alp_vp.viewmodel.AuthViewModel
import java.text.NumberFormat
import java.util.Locale

private val Background = Color(0xFF0F1115)
private val CardBackground = Color(0xFF14161A)
private val AccentBlue = Color(0xFF2B62FF)
private val AccentRed = Color(0xFFF85C5C)
private val AccentGreen = Color(0xFF57D06A)
private val TitleColor = Color(0xFFFFFFFF)
private val SubText = Color(0xFF98A0B3)

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToEditProfile: () -> Unit = {},
    profileViewModel: ProfileViewModel = viewModel()
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    val profileData by profileViewModel.profileData.collectAsState()
    val profileStats by profileViewModel.profileStats.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()
    val error by profileViewModel.error.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        if (isLoading && profileData == null) {
            // Initial loading state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AccentBlue)
            }
        } else if (error != null && profileData == null) {
            // Error state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "⚠️ Failed to load profile",
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
                    onClick = { profileViewModel.refresh() },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                ) {
                    Text("Retry")
                }
            }
        } else {
            // Main content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item { Spacer(modifier = Modifier.height(24.dp)) }

                // Profile Header
                item {
                    ProfileHeader(
                        username = profileData?.username ?: "User",
                        email = profileData?.email ?: "",
                        role = profileData?.role ?: "TALENT"
                    )
                }

                // Edit Profile Button
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onNavigateToEditProfile,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = AccentBlue
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Edit Profile", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                // Stats Card
                profileStats?.let { stats ->
                    item {
                        StatsCard(stats = stats)
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }

                // Bounties Section
                profileData?.bounties?.let { bounties ->
                    if (bounties.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "My Bounties",
                                count = bounties.size
                            )
                        }
                        items(bounties) { bounty ->
                            BountyItemCard(bounty = bounty)
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                }

                // Events Section
                profileData?.events?.let { events ->
                    if (events.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "My Events",
                                count = events.size
                            )
                        }
                        items(events) { event ->
                            EventItemCard(event = event)
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                }

                // Posts Section
                profileData?.posts?.let { posts ->
                    if (posts.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "My Posts",
                                count = posts.size
                            )
                        }
                        items(posts) { post ->
                            PostItemCard(post = post)
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                }

                // Logout Button
                item {
                    Button(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Logout", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Logout Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = CardBackground,
            title = { Text("Logout", color = TitleColor, fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to logout?", color = SubText) },
            confirmButton = {
                Button(
                    onClick = {
                        authViewModel.logout()
                        showLogoutDialog = false
                        onNavigateToLogin()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = SubText)
                }
            }
        )
    }
}

// Helper Composables

@Composable
private fun ProfileHeader(username: String, email: String, role: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Surface(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            color = AccentBlue.copy(alpha = 0.2f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(50.dp),
                    tint = AccentBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = username,
            color = TitleColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = email,
            color = SubText,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Role Badge
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (role == "COMPANY") AccentBlue.copy(alpha = 0.2f) else AccentGreen.copy(alpha = 0.2f)
        ) {
            Text(
                text = role,
                color = if (role == "COMPANY") AccentBlue else AccentGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun StatsCard(stats: ProfileStats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Statistics",
                color = TitleColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Grid
            Row(modifier = Modifier.fillMaxWidth()) {
                StatItem(
                    label = "Posts",
                    value = stats.totalPosts.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    label = "Events",
                    value = stats.totalEvents.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    label = "Bounties",
                    value = stats.totalBounties.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = SubText.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))

            // Earnings
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Total XP", color = SubText, fontSize = 12.sp)
                    Text(
                        text = NumberFormat.getNumberInstance().format(stats.totalXpEarned),
                        color = AccentBlue,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Total Earnings", color = SubText, fontSize = 12.sp)
                    Text(
                        text = "Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(stats.totalMoneyEarned)}",
                        color = AccentGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = TitleColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = SubText,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun SectionHeader(title: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = TitleColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AccentBlue.copy(alpha = 0.2f)
        ) {
            Text(
                text = count.toString(),
                color = AccentBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun BountyItemCard(bounty: ProfileBountyItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp)
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = bounty.company,
                        color = SubText,
                        fontSize = 14.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (bounty.status.uppercase()) {
                        "OPEN" -> AccentBlue.copy(alpha = 0.2f)
                        "CLOSED" -> Color.Gray.copy(alpha = 0.2f)
                        else -> AccentGreen.copy(alpha = 0.2f)
                    }
                ) {
                    Text(
                        text = bounty.status.uppercase(),
                        color = when (bounty.status.uppercase()) {
                            "OPEN" -> AccentBlue
                            "CLOSED" -> Color.Gray
                            else -> AccentGreen
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "💰", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(bounty.rewardMoney)}",
                        color = AccentGreen,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "⭐", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${bounty.rewardXp} XP",
                        color = AccentBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (bounty.isCompleted == true) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = AccentGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EventItemCard(event: EventItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = event.title,
                color = TitleColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.company_name,
                color = SubText,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = AccentBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.event_date.take(10),
                        color = SubText,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = "${event.current_registrations}/${event.registered_quota} registered",
                    color = SubText,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun PostItemCard(post: PostItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.content,
                color = TitleColor,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = post.created_at.take(10),
                    color = SubText,
                    fontSize = 12.sp
                )
                if (post.comments.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Comment,
                            contentDescription = "Comments",
                            tint = SubText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = post.comments.size.toString(),
                            color = SubText,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = SubText,
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = TitleColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

