package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBountyScreen(
    onNavigateBack: () -> Unit,
    onPublish: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var xpReward by remember { mutableStateOf("") }
    var minLevel by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var requirements by remember { mutableStateOf(listOf<String>()) }
    var requirementInput by remember { mutableStateOf("") }
    var isEvent by remember { mutableStateOf(false) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    val categoryOptions = listOf("Novice", "Expert", "Intermediate")
    var expandedCategory by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Bounty") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💼",
                        fontSize = 32.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Column {
                        Text(
                            text = "Post a Bounty",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Find talented freelancers for your project",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Bounty Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Bounty Title *") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g., Build Mobile App for E-Commerce") },
                singleLine = true
            )

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                placeholder = { Text("Describe the project, expected deliverables, and timeline...") },
                maxLines = 8
            )

            // Price & XP Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            price = it
                        }
                    },
                    label = { Text("Price (Rp) *") },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("15000000") },
                    singleLine = true,
                    prefix = { Text("Rp ") }
                )

                OutlinedTextField(
                    value = xpReward,
                    onValueChange = {
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            xpReward = it
                        }
                    },
                    label = { Text("XP Reward *") },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("500") },
                    singleLine = true,
                    suffix = { Text("XP") }
                )
            }

            // Min Level & Deadline Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = minLevel,
                    onValueChange = {
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            minLevel = it
                        }
                    },
                    label = { Text("Min Level *") },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("10") },
                    singleLine = true,
                    prefix = { Text("Lvl ") }
                )

                OutlinedTextField(
                    value = deadline,
                    onValueChange = { deadline = it },
                    label = { Text("Deadline *") },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("2025-12-31") },
                    singleLine = true
                )
            }

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = !expandedCategory }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Difficulty Level *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false }
                ) {
                    categoryOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                category = option
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            // Event Toggle
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isEvent)
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                    else
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🎯 Mark as Event Bounty",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Event bounties offer double XP!",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    Switch(
                        checked = isEvent,
                        onCheckedChange = { isEvent = it }
                    )
                }
            }

            // Requirements Section
            Text(
                text = "Requirements",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = requirementInput,
                    onValueChange = { requirementInput = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("e.g., 3+ years experience") },
                    singleLine = true
                )

                IconButton(
                    onClick = {
                        if (requirementInput.isNotBlank()) {
                            requirements = requirements + requirementInput.trim()
                            requirementInput = ""
                        }
                    },
                    modifier = Modifier.size(56.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Requirement",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            // Display Requirements
            if (requirements.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        requirements.forEach { requirement ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "•",
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = requirement,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                IconButton(
                                    onClick = { requirements = requirements - requirement },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Publish Button
            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank() &&
                        price.isNotBlank() && xpReward.isNotBlank() &&
                        minLevel.isNotBlank() && category.isNotBlank()) {
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = title.isNotBlank() && description.isNotBlank() &&
                         price.isNotBlank() && xpReward.isNotBlank() &&
                         minLevel.isNotBlank() && category.isNotBlank()
            ) {
                Text(
                    text = "Publish Bounty",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Success Dialog
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(
                        text = "🎉 Bounty Published!",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        Text("Your bounty is now live and visible to all talents!")
                        Spacer(modifier = Modifier.height(8.dp))
                        if (isEvent) {
                            Text(
                                text = "⚡ Event Bounty: Double XP Active!",
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            onPublish()
                        }
                    ) {
                        Text("View My Bounties")
                    }
                }
            )
        }
    }
}


