package com.jason.alp_vp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jason.alp_vp.ui.screens.*

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Screen.Bounties.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Bounties.route) {
            BountiesScreen(
                onBountyClick = { bountyId ->
                    navController.navigate(Screen.BountyDetails.createRoute(bountyId))
                },
                onNavigateToForums = {
                    navController.navigate(Screen.Forums.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(
            route = Screen.BountyDetails.route,
            arguments = listOf(
                navArgument("bountyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bountyId = backStackEntry.arguments?.getString("bountyId") ?: ""
            BountyDetailsScreen(
                bountyId = bountyId,
                onNavigateBack = { navController.popBackStack() },
                onApplyClick = { id ->
                    navController.navigate(Screen.BountyApply.createRoute(id))
                },
                onViewApplicantsClick = { id ->
                    navController.navigate(Screen.UserRegistered.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.BountyApply.route,
            arguments = listOf(
                navArgument("bountyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bountyId = backStackEntry.arguments?.getString("bountyId") ?: ""
            BountyApplyScreen(
                bountyId = bountyId,
                onNavigateBack = { navController.popBackStack() },
                onApplicationSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.BountySubmission.route,
            arguments = listOf(
                navArgument("bountyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bountyId = backStackEntry.arguments?.getString("bountyId") ?: ""
            BountySubmissionScreen(
                bountyId = bountyId,
                onNavigateBack = { navController.popBackStack() },
                onSubmissionSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.UserRegistered.route,
            arguments = listOf(
                navArgument("bountyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bountyId = backStackEntry.arguments?.getString("bountyId") ?: ""
            UserRegisteredScreen(
                bountyId = bountyId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Forums.route) {
            ForumsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileDashboardScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

