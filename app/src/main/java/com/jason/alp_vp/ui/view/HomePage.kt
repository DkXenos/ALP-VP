package com.jason.alp_vp.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jason.alp_vp.data.DummyDataRepository
import com.jason.alp_vp.ui.model.Bounty
import java.text.NumberFormat
import java.util.Locale

// Dark theme colors matching the prototype
private val BackgroundDark = Color(0xFF0A0E27)
private val CardBackgroundDark = Color(0xFF1A1F3A)
private val AccentBlue = Color(0xFF4A90E2)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFB0B8C4)
private val GreenReward = Color(0xFF4CAF50)
private val TabSelected = Color(0xFF2962FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    onBountyClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }

    // Get bounties from repository (replace with API call later)
    val allBounties = remember { DummyDataRepository.getAllBounties() }

    // Filter bounties based on search and tab
    val filteredBounties = remember(searchQuery, selectedTab) {
        val filtered = if (searchQuery.isNotEmpty()) {
            DummyDataRepository.searchBounties(searchQuery)
        } else {
            allBounties
        }

        // Further filter by tab type if needed
        when (selectedTab) {
            0 -> filtered // All
            1 -> filtered // Web
            2 -> filtered // Design
            else -> filtered
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Text(
                text = "BitBounty",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Tabs
            FilterTabs(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bounty List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredBounties) { bounty ->
                    BountyCard(
                        bounty = bounty,
                        onClick = { onBountyClick(bounty.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = {
            Text(
                text = "Search bounties...",
                color = TextSecondary
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = TextSecondary
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CardBackgroundDark,
            unfocusedContainerColor = CardBackgroundDark,
            focusedBorderColor = AccentBlue,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            cursorColor = AccentBlue,
            focusedPlaceholderColor = TextSecondary,
            unfocusedPlaceholderColor = TextSecondary
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
private fun FilterTabs(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("All", "Web", "Design")

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        tabs.forEachIndexed { index, title ->
            FilterChip(
                title = title,
                isSelected = selectedTab == index,
                onClick = { onTabSelected(index) }
            )
        }
    }
}

@Composable
private fun FilterChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) TabSelected else CardBackgroundDark
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun BountyCard(
    bounty: Bounty,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundDark
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Company name
            Text(
                text = bounty.company,
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = bounty.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Reward and Deadline Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reward
                Column {
                    Text(
                        text = "Reward",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // XP Badge
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AccentBlue.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "${bounty.rewardXp} XP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AccentBlue,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        // Money
                        Text(
                            text = formatCurrency(bounty.rewardMoney),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenReward
                        )
                    }
                }

                // Deadline
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Deadline",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = bounty.deadline,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

// Helper function to format currency in Indonesian Rupiah
private fun formatCurrency(amount: Int): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
    return formatter.format(amount).replace("Rp", "Rp ")
}

@Preview(
    name = "HomePage - Light",
    showBackground = true,
    backgroundColor = 0xFF0A0E27,
    showSystemUi = true
)
@Composable
fun HomePagePreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        HomePage()
    }
}

@Preview(
    name = "HomePage - With Search",
    showBackground = true,
    backgroundColor = 0xFF0A0E27,
    widthDp = 360,
    heightDp = 640
)
@Composable
fun HomePageWithSearchPreview() {
    var searchQuery by remember { mutableStateOf("E-Commerce") }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        HomePage()
    }
}

