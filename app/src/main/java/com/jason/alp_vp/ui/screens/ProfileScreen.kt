package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jason.alp_vp.utils.TokenManager
import com.jason.alp_vp.viewmodel.AuthViewModel

private val Background = Color(0xFF0F1115)
private val CardBackground = Color(0xFF14161A)
private val AccentBlue = Color(0xFF2B62FF)
private val AccentRed = Color(0xFFF85C5C)
private val TitleColor = Color(0xFFFFFFFF)
private val SubText = Color(0xFF98A0B3)

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Get user info from TokenManager
    val username = try { TokenManager.getUsername() } catch (e: Exception) { "User" }
    val userId = try { TokenManager.getUserId() } catch (e: Exception) { -1 }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Profile Avatar
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = AccentBlue.copy(alpha = 0.2f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        modifier = Modifier.size(60.dp),
                        tint = AccentBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Username
            Text(
                text = username ?: "User",
                color = TitleColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // User ID
            Text(
                text = "ID: $userId",
                color = SubText,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Profile Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Account Information",
                        color = TitleColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email/Username info
                    InfoRow(label = "Username", value = username ?: "N/A")

                    Spacer(modifier = Modifier.height(12.dp))

                    InfoRow(label = "Status", value = "Active")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentRed
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Logout",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Logout",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = CardBackground,
            title = {
                Text(
                    text = "Logout",
                    color = TitleColor,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to logout?",
                    color = SubText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        authViewModel.logout()
                        showLogoutDialog = false
                        onNavigateToLogin()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRed
                    )
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancel", color = SubText)
                }
            }
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = SubText,
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = TitleColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

