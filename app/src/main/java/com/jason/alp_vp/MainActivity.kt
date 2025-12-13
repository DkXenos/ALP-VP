package com.jason.alp_vp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.ui.screens.LoginScreen
import com.jason.alp_vp.ui.screens.RegisterScreen
import com.jason.alp_vp.ui.theme.ALPVPTheme
import com.jason.alp_vp.ui.view.HomePage
import com.jason.alp_vp.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ALPVPTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AuthNavigationWrapper()
                }
            }
        }
    }
}

@Composable
fun AuthNavigationWrapper() {
    val authViewModel: AuthViewModel = viewModel()
    var currentScreen by remember { mutableStateOf("login") }

    // Check if user is already logged in on app start
    LaunchedEffect(Unit) {
        if (authViewModel.checkLoginStatus()) {
            currentScreen = "home"
        }
    }

    when (currentScreen) {
        "login" -> {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    currentScreen = "home"
                },
                onNavigateToRegister = {
                    currentScreen = "register"
                }
            )
        }

        "register" -> {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    currentScreen = "home"
                },
                onNavigateToLogin = {
                    currentScreen = "login"
                }
            )
        }

        "home" -> {
            HomePage(
                onBountyClick = { bountyId ->
                    // TODO: Navigate to bounty detail
                    println("Clicked bounty: $bountyId")
                }
            )
        }
    }
}
