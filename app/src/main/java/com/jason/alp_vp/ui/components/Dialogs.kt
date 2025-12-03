package com.jason.alp_vp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SuccessDialog(
    title: String,
    message: String,
    xpEarned: String? = null,
    isEvent: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = onConfirm
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(message)
                if (xpEarned != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "+$xpEarned XP Earned!",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (isEvent) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "⚡ Event Bounty: Double XP Active!",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("OK")
            }
        }
    )
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    isDestructive: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = if (isDestructive) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                } else {
                    ButtonDefaults.buttonColors()
                }
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        }
    )
}

@Composable
fun LoadingDialog(
    message: String = "Loading..."
) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = message,
                    fontSize = 16.sp
                )
            }
        },
        confirmButton = { }
    )
}

