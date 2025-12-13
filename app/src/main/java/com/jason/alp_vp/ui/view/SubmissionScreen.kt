package com.jason.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jason.alp_vp.data.DummyDataRepository

// Dark theme colors
private val BackgroundDark = Color(0xFF0A0E27)
private val CardBackgroundDark = Color(0xFF1A1F3A)
private val AccentBlue = Color(0xFF4A90E2)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFB0B8C4)
private val ButtonBlue = Color(0xFF2962FF)
private val BorderColor = Color(0xFF2A2F4A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationFormPage(
    bountyId: String,
    bountyTitle: String = "Bounty Application",
    onBackClick: () -> Unit = {},
    onSubmitSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Form state
    var portfolioLinks by remember { mutableStateOf(listOf("", "", "")) }
    var cvImageUrl by remember { mutableStateOf("") }
    var whyHireYou by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            TopAppBar(
                title = {
                    Text(
                        text = "Apply for Bounty",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CardBackgroundDark
                )
            )

            // Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                // Header
                item {
                    Column {
                        Text(
                            text = "Application Form",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Fill in your details below to apply for this bounty",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Portfolio Links Section
                item {
                    PortfolioLinksSection(
                        links = portfolioLinks,
                        onLinksChange = { portfolioLinks = it }
                    )
                }

                // CV Upload Section
                item {
                    CVUploadSection(
                        cvUrl = cvImageUrl,
                        onCvUrlChange = { cvImageUrl = it }
                    )
                }

                // Why Should We Hire You Section
                item {
                    WhyHireYouSection(
                        text = whyHireYou,
                        onTextChange = { whyHireYou = it }
                    )
                }

                // Error message
                if (showError) {
                    item {
                        Text(
                            text = "Please fill in all required fields",
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }

            // Submit Button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardBackgroundDark,
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = {
                        // Validate form
                        val hasValidPortfolio = portfolioLinks.any { it.isNotBlank() }
                        val hasCV = cvImageUrl.isNotBlank()
                        val hasWhyHire = whyHireYou.isNotBlank()

                        if (hasValidPortfolio && hasCV && hasWhyHire) {
                            isSubmitting = true
                            showError = false

                            // Submit application (replace with API call)
                            val success = DummyDataRepository.submitApplication(
                                bountyId = bountyId,
                                portfolioLinks = portfolioLinks.filter { it.isNotBlank() },
                                cvImageUrl = cvImageUrl,
                                whyHireYou = whyHireYou
                            )

                            if (success) {
                                onSubmitSuccess()
                            }
                            isSubmitting = false
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isSubmitting
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = TextPrimary
                        )
                    } else {
                        Text(
                            text = "Submit Application",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PortfolioLinksSection(
    links: List<String>,
    onLinksChange: (List<String>) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Portfolio Links",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Add up to 3 links to your portfolio or projects",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            links.forEachIndexed { index, link ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = link,
                        onValueChange = { newValue ->
                            val newLinks = links.toMutableList()
                            newLinks[index] = newValue
                            onLinksChange(newLinks)
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                text = "Link Portfolio ${index + 1}",
                                color = TextSecondary
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = BackgroundDark,
                            unfocusedContainerColor = BackgroundDark,
                            focusedBorderColor = AccentBlue,
                            unfocusedBorderColor = BorderColor,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = AccentBlue
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }
                if (index < links.size - 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun CVUploadSection(
    cvUrl: String,
    onCvUrlChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "CV / Resume",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Upload your CV or provide a link (Image/PDF URL)",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CV Image/Link placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(
                        width = 2.dp,
                        color = if (cvUrl.isBlank()) BorderColor else AccentBlue,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(BackgroundDark, RoundedCornerShape(8.dp))
                    .clickable { /* TODO: Open file picker */ },
                contentAlignment = Alignment.Center
            ) {
                if (cvUrl.isBlank()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Upload CV",
                            tint = TextSecondary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Click to upload or paste URL",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                } else {
                    Text(
                        text = "CV Uploaded",
                        color = AccentBlue,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = cvUrl,
                onValueChange = onCvUrlChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Or paste CV URL here",
                        color = TextSecondary
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = BackgroundDark,
                    unfocusedContainerColor = BackgroundDark,
                    focusedBorderColor = AccentBlue,
                    unfocusedBorderColor = BorderColor,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = AccentBlue
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WhyHireYouSection(
    text: String,
    onTextChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Why Should We Hire You?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tell us why you're the best fit for this bounty",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                placeholder = {
                    Text(
                        text = "Describe your experience, skills, and why you're interested in this bounty...",
                        color = TextSecondary
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = BackgroundDark,
                    unfocusedContainerColor = BackgroundDark,
                    focusedBorderColor = AccentBlue,
                    unfocusedBorderColor = BorderColor,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = AccentBlue
                ),
                shape = RoundedCornerShape(8.dp),
                maxLines = 8
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ApplicationFormPagePreview() {
    ApplicationFormPage(
        bountyId = "1",
        bountyTitle = "Build E-Commerce Mobile App"
    )
}
