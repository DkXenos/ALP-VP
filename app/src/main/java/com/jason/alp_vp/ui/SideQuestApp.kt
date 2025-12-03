package com.jason.alp_vp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jason.alp_vp.navigation.Screen
import com.jason.alp_vp.ui.screens.*

sealed class BottomNavScreen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : BottomNavScreen(Screen.Bounties.route, "Home", Icons.Default.Home)
    object Active : BottomNavScreen(Screen.Active.route, "Active", Icons.Default.CheckCircle)
    object Forums : BottomNavScreen(Screen.Forums.route, "Forums", Icons.Default.Search)
    object Profile : BottomNavScreen(Screen.Profile.route, "Profile", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideQuestApp(startDestination: String = Screen.Auth.route) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Screens that should show bottom navigation
    val bottomNavScreens = listOf(
        Screen.Bounties.route,
        Screen.Active.route,
        Screen.Forums.route,
        Screen.Profile.route
    )

    val shouldShowBottomBar = currentDestination?.route in bottomNavScreens

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    val items = listOf(
                        BottomNavScreen.Home,
                        BottomNavScreen.Active,
                        BottomNavScreen.Forums,
                        BottomNavScreen.Profile
                    )

                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                        navController.navigate(Screen.Forums.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(Screen.Active.route) {
                ActiveBountiesScreen(
                    onBountyClick = { bountyId ->
                        navController.navigate(Screen.ActiveBountyDetails.createRoute(bountyId))
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
                    onNavigateBack = { navController.popBackStack() },
                    onPostClick = { postId ->
                        navController.navigate(Screen.PostDetails.createRoute(postId))
                    },
                    onEventRegister = { eventId ->
                        // Event registration successful - stay on Forums page
                        // In a real app, this would call an API
                        // For now, the button works but stays on same page
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileDashboardScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onPortfolioClick = { index ->
                        navController.navigate(Screen.PortfolioDetails.createRoute(index))
                    },
                    onAddPortfolio = {
                        navController.navigate(Screen.AddPortfolio.route)
                    },
                    onLogout = {
                        // Clear user session
                        com.jason.alp_vp.repository.MockRepository.logout()
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = Screen.PostDetails.route,
                arguments = listOf(
                    navArgument("postId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val postId = backStackEntry.arguments?.getString("postId") ?: ""
                PostDetailsScreen(
                    postId = postId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.PortfolioDetails.route,
                arguments = listOf(
                    navArgument("portfolioIndex") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val portfolioIndex = backStackEntry.arguments?.getInt("portfolioIndex") ?: 0
                PortfolioDetailsScreen(
                    portfolioIndex = portfolioIndex,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.AddPortfolio.route) {
                AddPortfolioScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onSave = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.ActiveBountyDetails.route,
                arguments = listOf(
                    navArgument("bountyId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val bountyId = backStackEntry.arguments?.getString("bountyId") ?: ""
                ActiveBountyDetailsScreen(
                    bountyId = bountyId,
                    onNavigateBack = { navController.popBackStack() },
                    onSubmit = { id ->
                        navController.navigate(Screen.BountySubmission.createRoute(id))
                    }
                )
            }

            composable(Screen.CreateBounty.route) {
                CreateBountyScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onPublish = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.ApplicantDetails.route,
                arguments = listOf(
                    navArgument("applicantId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val applicantId = backStackEntry.arguments?.getString("applicantId") ?: ""
                ApplicantDetailsScreen(
                    applicantId = applicantId,
                    onNavigateBack = { navController.popBackStack() },
                    onAccept = {
                        // Handle accept logic
                        navController.popBackStack()
                    },
                    onReject = {
                        // Handle reject logic
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

