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
import com.jason.alp_vp.ui.model.WalletTransaction
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WalletHistoryList(
    transactions: List<WalletTransaction>,
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
            Text(
                text = "Transaction History",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (transactions.isEmpty()) {
                EmptyTransactionHistoryState()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(transactions) { transaction ->
                        TransactionItem(transaction = transaction)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyTransactionHistoryState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "📋",
            fontSize = 48.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No transactions yet",
            color = Color(0xFF98A0B3),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your transaction history will appear here",
            color = Color(0xFF98A0B3),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TransactionItem(
    transaction: WalletTransaction
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
                // Transaction Icon
                Text(
                    text = when (transaction.type) {
                        "earned" -> "💰"
                        "withdrawn" -> "💸"
                        else -> "💳"
                    },
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = transaction.description,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = formatDate(transaction.createdAt),
                            color = Color(0xFF98A0B3),
                            fontSize = 12.sp
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (transaction.status) {
                                "completed" -> Color(0xFF57D06A).copy(alpha = 0.2f)
                                "pending" -> Color(0xFFFFB800).copy(alpha = 0.2f)
                                "failed" -> Color(0xFFF85C5C).copy(alpha = 0.2f)
                                else -> Color(0xFF98A0B3).copy(alpha = 0.2f)
                            }
                        ) {
                            Text(
                                text = transaction.status.uppercase(),
                                color = when (transaction.status) {
                                    "completed" -> Color(0xFF57D06A)
                                    "pending" -> Color(0xFFFFB800)
                                    "failed" -> Color(0xFFF85C5C)
                                    else -> Color(0xFF98A0B3)
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Amount
            Text(
                text = "${if (transaction.amount > 0) "+" else ""}Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(kotlin.math.abs(transaction.amount))}",
                color = if (transaction.amount > 0) Color(0xFF57D06A) else Color(0xFFF85C5C),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun formatDate(instant: Instant): String {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF0A0B0F)
@Composable
fun WalletHistoryListPreview() {
    WalletHistoryList(transactions = emptyList())
}
