package com.jason.alp_vp.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jason.alp_vp.ui.screens.LoginScreen
import com.jason.alp_vp.ui.screens.RegisterScreen
import com.jason.alp_vp.ui.screens.SuccessScreen
import com.jason.alp_vp.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController, authViewModel)
        }

        composable("register") {
            RegisterScreen(navController, authViewModel)
        }

        composable("success") {
            SuccessScreen()
        }
    }
}
