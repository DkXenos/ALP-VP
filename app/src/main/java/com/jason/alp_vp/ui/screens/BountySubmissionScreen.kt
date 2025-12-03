package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.viewmodel.SubmissionState
import com.jason.alp_vp.viewmodel.SubmissionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BountySubmissionScreen(
    bountyId: String,
    onNavigateBack: () -> Unit,
    onSubmissionSuccess: () -> Unit,
    viewModel: SubmissionViewModel = viewModel()
) {
    var submissionLink by remember { mutableStateOf("") }
    var submissionNotes by remember { mutableStateOf("") }
    val submissionState by viewModel.submissionState.collectAsState()

    LaunchedEffect(submissionState) {
        if (submissionState is SubmissionState.Success) {
            onSubmissionSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Submit Your Work") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Submit Your Work",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Provide a link to your completed work and add any notes for the client",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Submission Link Input
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Submission Link *",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = submissionLink,
                        onValueChange = { submissionLink = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("https://github.com/yourproject or Google Drive link...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "💡 Examples: GitHub repository, Google Drive, Figma link, deployed website URL",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        lineHeight = 16.sp
                    )
                }
            }

            // Submission Notes
            OutlinedTextField(
                value = submissionNotes,
                onValueChange = { submissionNotes = it },
                label = { Text("Additional Notes (Optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                placeholder = {
                    Text("Add any notes about your submission, challenges faced, or additional details...")
                },
                maxLines = 8,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.submitWork(bountyId, "Link: $submissionLink\nNotes: $submissionNotes")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = submissionState !is SubmissionState.Loading && submissionLink.isNotBlank()
            ) {
                if (submissionState is SubmissionState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Mark as Done",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (submissionState is SubmissionState.Error) {
                Text(
                    text = (submissionState as SubmissionState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

