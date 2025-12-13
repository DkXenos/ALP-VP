package com.jason.alp_vp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jason.alp_vp.ui.screens.LoginScreen
import com.jason.alp_vp.ui.screens.RegisterScreen
import com.jason.alp_vp.ui.screens.SuccessScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("success") {
            SuccessScreen()
        }
    }
}
