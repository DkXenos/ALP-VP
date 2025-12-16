package com.jason.alp_vp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.ui.viewmodel.CreatePageViewModel
import com.jason.alp_vp.ui.viewmodel.CreateType
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CreatePage(
    onNavigateBack: () -> Unit = {},
    viewModel: CreatePageViewModel = viewModel()
) {
    val selectedType by viewModel.selectedType.collectAsState()
    val bountyForm by viewModel.bountyFormData.collectAsState()
    val eventForm by viewModel.eventFormData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val walletBalance by viewModel.walletBalance.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Success message
        successMessage?.let { msg ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1D3D1A)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = msg,
                    color = Color(0xFF57D06A),
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

            // Wallet Balance Card
            WalletBalanceDisplay(balance = walletBalance)

            Spacer(modifier = Modifier.height(16.dp))

            // Type Selector (Radio Buttons)
            TypeSelectorCard(
                selectedType = selectedType,
                onTypeSelected = { viewModel.selectType(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic Form based on selected type
            when (selectedType) {
                CreateType.BOUNTY -> BountyForm(
                    formData = bountyForm,
                    onTitleChange = { viewModel.updateBountyForm(title = it) },
                    onDeadlineChange = { viewModel.updateBountyForm(deadline = it) },
                    onRewardXpChange = { viewModel.updateBountyForm(rewardXp = it) },
                    onRewardMoneyChange = { viewModel.updateBountyForm(rewardMoney = it) },
                    onDescriptionChange = { viewModel.updateBountyForm(description = it) }
                )
                CreateType.EVENT -> EventForm(
                    formData = eventForm,
                    onTitleChange = { viewModel.updateEventForm(title = it) },
                    onDescriptionChange = { viewModel.updateEventForm(description = it) },
                    onEventDateChange = { viewModel.updateEventForm(eventDate = it) },
                    onQuotaChange = { viewModel.updateEventForm(registeredQuota = it) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error Message
            errorMessage?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF85C5C).copy(alpha = 0.2f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = error,
                        color = Color(0xFFF85C5C),
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Create Button
            Button(
                onClick = {
                    when (selectedType) {
                        CreateType.BOUNTY -> viewModel.createBounty()
                        CreateType.EVENT -> viewModel.createEvent()
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2B62FF)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "CREATE ${if (selectedType == CreateType.BOUNTY) "BOUNTY" else "EVENT"}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun WalletBalanceDisplay(balance: Double) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14161A)),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Wallet Balance",
                    color = Color(0xFF98A0B3),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Rp ${NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID")).format(balance)}",
                    color = Color(0xFF57D06A),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(text = "💰", fontSize = 32.sp)
        }
    }
}

@Composable
private fun TypeSelectorCard(
    selectedType: CreateType,
    onTypeSelected: (CreateType) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14161A)),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Select Type",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Bounty Radio Button
                Surface(
                    onClick = { onTypeSelected(CreateType.BOUNTY) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (selectedType == CreateType.BOUNTY)
                        Color(0xFF2B62FF).copy(alpha = 0.2f)
                    else
                        Color(0xFF1E2328),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedType == CreateType.BOUNTY,
                            onClick = { onTypeSelected(CreateType.BOUNTY) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF2B62FF),
                                unselectedColor = Color(0xFF98A0B3)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Bounty",
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "💰 Paid task",
                                color = Color(0xFF98A0B3),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Event Radio Button
                Surface(
                    onClick = { onTypeSelected(CreateType.EVENT) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (selectedType == CreateType.EVENT)
                        Color(0xFF2B62FF).copy(alpha = 0.2f)
                    else
                        Color(0xFF1E2328),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedType == CreateType.EVENT,
                            onClick = { onTypeSelected(CreateType.EVENT) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF2B62FF),
                                unselectedColor = Color(0xFF98A0B3)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Event",
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "📅 Free event",
                                color = Color(0xFF98A0B3),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BountyForm(
    formData: com.jason.alp_vp.ui.viewmodel.BountyFormData,
    onTitleChange: (String) -> Unit,
    onDeadlineChange: (String) -> Unit,
    onRewardXpChange: (String) -> Unit,
    onRewardMoneyChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14161A)),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Bounty Details",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            OutlinedTextField(
                value = formData.title,
                onValueChange = onTitleChange,
                label = { Text("Title *", color = Color(0xFF98A0B3)) },
                placeholder = { Text("Enter bounty title", color = Color(0xFF98A0B3)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2B62FF),
                    unfocusedBorderColor = Color(0xFF98A0B3),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            OutlinedTextField(
                value = formData.description,
                onValueChange = onDescriptionChange,
                label = { Text("Description", color = Color(0xFF98A0B3)) },
                placeholder = { Text("Enter bounty description", color = Color(0xFF98A0B3)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2B62FF),
                    unfocusedBorderColor = Color(0xFF98A0B3),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Deadline
            OutlinedTextField(
                value = formData.deadline,
                onValueChange = onDeadlineChange,
                label = { Text("Deadline (YYYY-MM-DD) *", color = Color(0xFF98A0B3)) },
                placeholder = { Text("2024-12-31", color = Color(0xFF98A0B3)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2B62FF),
                    unfocusedBorderColor = Color(0xFF98A0B3),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Reward XP
                OutlinedTextField(
                    value = formData.rewardXp,
                    onValueChange = onRewardXpChange,
                    label = { Text("Reward XP", color = Color(0xFF98A0B3)) },
                    placeholder = { Text("100", color = Color(0xFF98A0B3)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2B62FF),
                        unfocusedBorderColor = Color(0xFF98A0B3),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )

                // Reward Money
                OutlinedTextField(
                    value = formData.rewardMoney,
                    onValueChange = onRewardMoneyChange,
                    label = { Text("Reward (Rp) *", color = Color(0xFF98A0B3)) },
                    placeholder = { Text("5000", color = Color(0xFF98A0B3)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2B62FF),
                        unfocusedBorderColor = Color(0xFF98A0B3),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "* Required fields",
                color = Color(0xFF98A0B3),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun EventForm(
    formData: com.jason.alp_vp.ui.viewmodel.EventFormData,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onEventDateChange: (String) -> Unit,
    onQuotaChange: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14161A)),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Event Details",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            OutlinedTextField(
                value = formData.title,
                onValueChange = onTitleChange,
                label = { Text("Event Title *", color = Color(0xFF98A0B3)) },
                placeholder = { Text("Enter event title", color = Color(0xFF98A0B3)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2B62FF),
                    unfocusedBorderColor = Color(0xFF98A0B3),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            OutlinedTextField(
                value = formData.description,
                onValueChange = onDescriptionChange,
                label = { Text("Description *", color = Color(0xFF98A0B3)) },
                placeholder = { Text("Enter event description", color = Color(0xFF98A0B3)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2B62FF),
                    unfocusedBorderColor = Color(0xFF98A0B3),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Event Date
            OutlinedTextField(
                value = formData.eventDate,
                onValueChange = onEventDateChange,
                label = { Text("Event Date (YYYY-MM-DD) *", color = Color(0xFF98A0B3)) },
                placeholder = { Text("2024-12-31", color = Color(0xFF98A0B3)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2B62FF),
                    unfocusedBorderColor = Color(0xFF98A0B3),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Registered Quota
            OutlinedTextField(
                value = formData.registeredQuota,
                onValueChange = onQuotaChange,
                label = { Text("Registration Quota *", color = Color(0xFF98A0B3)) },
                placeholder = { Text("50", color = Color(0xFF98A0B3)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2B62FF),
                    unfocusedBorderColor = Color(0xFF98A0B3),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "* Required fields",
                color = Color(0xFF98A0B3),
                fontSize = 12.sp
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF0A0B0F)
@Composable
fun CreatePagePreview() {
    CreatePage()
}

