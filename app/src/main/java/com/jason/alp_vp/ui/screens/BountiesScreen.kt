package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.model.Bounty
import com.jason.alp_vp.ui.components.BountyCard
import com.jason.alp_vp.viewmodel.BountyViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BountiesScreen(
    onBountyClick: (String) -> Unit,
    onNavigateToForums: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onCreateBounty: () -> Unit = {},
    viewModel: BountyViewModel = viewModel()
) {
    val bounties by viewModel.bounties.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val currentUser by com.jason.alp_vp.repository.MockRepository.currentUser.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SideQuest") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            // Show Create Bounty button only for companies
            if (currentUser?.role == com.jason.alp_vp.model.UserRole.COMPANY) {
                FloatingActionButton(
                    onClick = onCreateBounty,
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Bounty"
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchBounties(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search bounties...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Filter Chips
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listOf("All", "Novice", "Expert")) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { viewModel.applyFilter(filter) },
                        label = { Text(filter) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bounties List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(bounties) { bounty ->
                    BountyCard(
                        bounty = bounty,
                        onClick = { onBountyClick(bounty.id) }
                    )
                }
            }
        }
    }
}

