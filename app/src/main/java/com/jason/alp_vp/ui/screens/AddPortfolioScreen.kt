package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
fun AddPortfolioScreen(
    onNavigateBack: () -> Unit,
    onSave: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var xpEarned by remember { mutableStateOf("") }
    var technologies by remember { mutableStateOf(listOf<String>()) }
    var techInput by remember { mutableStateOf("") }
    var achievements by remember { mutableStateOf(listOf<String>()) }
    var achievementInput by remember { mutableStateOf("") }

    var showSuccessDialog by remember { mutableStateOf(false) }

    val categoryOptions = listOf(
        "Mobile Development",
        "UI/UX Design",
        "Backend Development",
        "Web Development",
        "Content Writing",
        "Marketing",
        "Game Development",
        "Data Science"
    )

    var expandedCategory by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Portfolio Project") },
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
            Text(
                text = "Showcase Your Achievement",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Add a completed project to your portfolio to showcase your skills and earn recognition.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Project Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Project Title *") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g., E-Commerce Mobile App") },
                singleLine = true
            )

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = !expandedCategory }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category *") },
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

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Project Description *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                placeholder = { Text("Describe what you built and the impact it had...") },
                maxLines = 6
            )

            // XP Earned
            OutlinedTextField(
                value = xpEarned,
                onValueChange = {
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        xpEarned = it
                    }
                },
                label = { Text("XP Earned") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g., 500") },
                singleLine = true,
                suffix = { Text("XP") }
            )

            // Technologies Section
            Text(
                text = "Technologies Used",
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
                    value = techInput,
                    onValueChange = { techInput = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("e.g., Kotlin") },
                    singleLine = true
                )

                IconButton(
                    onClick = {
                        if (techInput.isNotBlank()) {
                            technologies = technologies + techInput.trim()
                            techInput = ""
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Technology",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            // Display Technologies
            if (technologies.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    technologies.forEach { tech ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = tech,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                IconButton(
                                    onClick = { technologies = technologies - tech },
                                    modifier = Modifier.size(16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Achievements Section
            Text(
                text = "Key Achievements",
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
                    value = achievementInput,
                    onValueChange = { achievementInput = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("e.g., Achieved 99.9% uptime") },
                    singleLine = true
                )

                IconButton(
                    onClick = {
                        if (achievementInput.isNotBlank()) {
                            achievements = achievements + achievementInput.trim()
                            achievementInput = ""
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
                            contentDescription = "Add Achievement",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            // Display Achievements
            if (achievements.isNotEmpty()) {
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
                        achievements.forEach { achievement ->
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
                                        text = "✓",
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = achievement,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                IconButton(
                                    onClick = { achievements = achievements - achievement },
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

            // Save Button
            Button(
                onClick = {
                    if (title.isNotBlank() && category.isNotBlank() && description.isNotBlank()) {
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = title.isNotBlank() && category.isNotBlank() && description.isNotBlank()
            ) {
                Text(
                    text = "Save Portfolio Project",
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
                        text = "🎉 Portfolio Added!",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        Text("Your project has been added to your portfolio successfully!")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "+${xpEarned.ifEmpty { "100" }} XP Earned!",
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            onSave()
                        }
                    ) {
                        Text("View Portfolio")
                    }
                }
            )
        }
    }
}

