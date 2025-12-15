package com.jason.alp_vp.ui.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jason.alp_vp.ui.model.PaymentMethod

@Composable
fun PaymentMethodsCard(
    paymentMethods: List<PaymentMethod>,
    onAddPaymentMethod: () -> Unit,
    onRemovePaymentMethod: (PaymentMethod) -> Unit,
    modifier: Modifier = Modifier
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Payment Methods",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                TextButton(
                    onClick = onAddPaymentMethod
                ) {
                    Text(
                        text = "+ Add",
                        color = Color(0xFF2B62FF),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (paymentMethods.isEmpty()) {
                EmptyPaymentMethodsState(onAddPaymentMethod)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(paymentMethods) { method ->
                        PaymentMethodItem(
                            paymentMethod = method,
                            onRemove = { onRemovePaymentMethod(method) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyPaymentMethodsState(
    onAddPaymentMethod: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "💳",
            fontSize = 48.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No payment methods added",
            color = Color(0xFF98A0B3),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Add a payment method to withdraw funds",
            color = Color(0xFF98A0B3),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAddPaymentMethod,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2B62FF)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Add Payment Method",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun PaymentMethodItem(
    paymentMethod: PaymentMethod,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2328)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Payment Method Icon
                Text(
                    text = when (paymentMethod.type) {
                        "bank" -> "🏦"
                        "ewallet" -> "📱"
                        "credit_card" -> "💳"
                        else -> "💰"
                    },
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = paymentMethod.name,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        if (paymentMethod.isDefault) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF57D06A).copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "DEFAULT",
                                    color = Color(0xFF57D06A),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = paymentMethod.accountNumber,
                        color = Color(0xFF98A0B3),
                        fontSize = 14.sp
                    )
                }
            }

            if (!paymentMethod.isDefault) {
                TextButton(
                    onClick = onRemove
                ) {
                    Text(
                        text = "Remove",
                        color = Color(0xFFF85C5C),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF0A0B0F)
@Composable
fun PaymentMethodsCardPreview() {
    PaymentMethodsCard(
        paymentMethods = emptyList(),
        onAddPaymentMethod = {},
        onRemovePaymentMethod = {}
    )
}
