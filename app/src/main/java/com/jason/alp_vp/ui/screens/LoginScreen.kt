package com.jason.alp_vp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jason.alp_vp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(nav: NavController) {
    val vm = remember { AuthViewModel() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authResponse by vm.authResponse.collectAsState()
    val error by vm.error.collectAsState()

    LaunchedEffect(authResponse) {
        if (authResponse != null) {
            nav.navigate("success")
        }
    }

    Column(modifier = Modifier.padding(20.dp)) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(Modifier.height(20.dp))

        Button(onClick = { vm.login(email, password) }, modifier = Modifier.fillMaxWidth()) {
            Text("Login")
        }

        if (error != null) {
            Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(20.dp))

        TextButton(onClick = { nav.navigate("register") }) {
            Text("Belum punya akun? Register")
        }
    }
}
