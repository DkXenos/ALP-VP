package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jason.alp_vp.viewmodel.AuthViewModel

// Dark theme colors
private val Background = Color(0xFF0F1115)
private val CardBackground = Color(0xFF14161A)
private val AccentBlue = Color(0xFF2F6BFF)
private val TitleColor = Color(0xFFFFFFFF)
private val SubText = Color(0xFF98A0B3)

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedAccountType by remember { mutableStateOf("USER") }

    val error by authViewModel.error.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // Navigate to Forum when successfully logged in
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate("Forum") {
                popUpTo("Login") { inclusive = true }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineLarge,
                color = TitleColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Sign in to continue",
                color = SubText,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(32.dp))

            // Role Selection with Radio Buttons
            Card(
                colors = CardDefaults.cardColors(CardBackground),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Login as:",
                        color = TitleColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // User Radio Option
                        RoleOption(
                            text = "👤 User",
                            selected = selectedAccountType == "USER",
                            onClick = { selectedAccountType = "USER" },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading
                        )

                        // Company Radio Option
                        RoleOption(
                            text = "🏢 Company",
                            selected = selectedAccountType == "COMPANY",
                            onClick = { selectedAccountType = "COMPANY" },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TitleColor,
                    unfocusedTextColor = TitleColor,
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground,
                    focusedBorderColor = AccentBlue,
                    unfocusedBorderColor = SubText.copy(alpha = 0.3f),
                    focusedLabelColor = AccentBlue,
                    unfocusedLabelColor = SubText
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TitleColor,
                    unfocusedTextColor = TitleColor,
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground,
                    focusedBorderColor = AccentBlue,
                    unfocusedBorderColor = SubText.copy(alpha = 0.3f),
                    focusedLabelColor = AccentBlue,
                    unfocusedLabelColor = SubText
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = { authViewModel.login(email, password, selectedAccountType) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBlue,
                    disabledContainerColor = SubText.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Error Message
            if (error != null) {
                Spacer(Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(Color(0xFF3D1515)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = error ?: "",
                        color = Color(0xFFF85C5C),
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Register Link
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have an account?", color = SubText, fontSize = 14.sp)
                TextButton(onClick = { navController.navigate("Register") }) {
                    Text("Register", color = AccentBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun RoleOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Card(
        modifier = modifier
            .selectable(
                selected = selected,
                enabled = enabled,
                onClick = onClick
            )
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) AccentBlue else SubText.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) AccentBlue.copy(alpha = 0.15f) else Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            RadioButton(
                selected = selected,
                onClick = null,
                enabled = enabled,
                colors = RadioButtonDefaults.colors(
                    selectedColor = AccentBlue,
                    unselectedColor = SubText
                )
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = text,
                color = if (selected) AccentBlue else TitleColor,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
