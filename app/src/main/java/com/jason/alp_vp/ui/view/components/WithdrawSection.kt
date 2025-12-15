package com.jason.alp_vp.ui.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jason.alp_vp.ui.model.PaymentMethod

@Composable
fun WithdrawSection(
    availableBalance: Double,
    paymentMethods: List<PaymentMethod>,
    onWithdraw: (Double, String) -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    var withdrawAmount by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf<PaymentMethod?>(null) }
    var showPaymentMethodDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14161A)),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Withdraw Money",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Amount Input
            OutlinedTextField(
                value = withdrawAmount,
                onValueChange = {
                    withdrawAmount = it
                    errorMessage = null
                },
                label = { Text("Amount (Rp)", color = Color(0xFF98A0B3)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2B62FF),
                    unfocusedBorderColor = Color(0xFF98A0B3),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Payment Method Selection
            OutlinedButton(
                onClick = { showPaymentMethodDialog = true },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (selectedPaymentMethod != null) Color.White else Color(0xFF98A0B3)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(0xFF98A0B3)
                    ).brush
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedPaymentMethod?.let { "${it.name} - ${it.accountNumber}" }
                        ?: "Select Payment Method",
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Error Message
            errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = Color(0xFFF85C5C),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Withdraw Button
            Button(
                onClick = {
                    val amount = withdrawAmount.toDoubleOrNull()
                    when {
                        amount == null || amount <= 0 -> {
                            errorMessage = "Please enter a valid amount"
                        }
                        amount > availableBalance -> {
                            errorMessage = "Insufficient balance"
                        }
                        selectedPaymentMethod == null -> {
                            errorMessage = "Please select a payment method"
                        }
                        else -> {
                            selectedPaymentMethod?.let { paymentMethod ->
                                onWithdraw(amount, paymentMethod.id)
                                withdrawAmount = ""
                                selectedPaymentMethod = null
                            }
                        }
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF57D06A)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = "Withdraw",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Payment Method Selection Dialog
    if (showPaymentMethodDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentMethodDialog = false },
            title = {
                Text("Select Payment Method", color = Color.White)
            },
            text = {
                Column {
                    paymentMethods.forEach { method ->
                        Card(
                            onClick = {
                                selectedPaymentMethod = method
                                showPaymentMethodDialog = false
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1E2328)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = method.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = method.accountNumber,
                                    color = Color(0xFF98A0B3),
                                    fontSize = 14.sp
                                )
                                if (method.isDefault) {
                                    Text(
                                        text = "Default",
                                        color = Color(0xFF57D06A),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showPaymentMethodDialog = false }
                ) {
                    Text("Cancel", color = Color(0xFF2B62FF))
                }
            },
            containerColor = Color(0xFF14161A)
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF0A0B0F)
@Composable
fun WithdrawSectionPreview() {
    WithdrawSection(
        availableBalance = 15250.75,
        paymentMethods = emptyList(),
        onWithdraw = { _, _ -> }
    )
}
