package com.jason.alp_vp.ui.route

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.jason.alp_vp.ui.screens.LoginScreen
import com.jason.alp_vp.ui.screens.ProfileScreen
import com.jason.alp_vp.ui.screens.RegisterScreen
import com.jason.alp_vp.ui.view.*
import com.jason.alp_vp.ui.view.HomePage
import com.jason.alp_vp.ui.viewmodel.ForumPageViewModel

data class BottomNavItem(
    val view: AppView,
    val label: String
)

enum class AppView(
    val title: String,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null
) {
    Login("Login"),
    Register("Register"),
    Forum("Homepage", Icons.Filled.Home, Icons.Outlined.Home),
    Posts("Posts", Icons.AutoMirrored.Filled.List, Icons.AutoMirrored.Outlined.List),
    PostDetail("PostDetail"),
    Events("Events"),
    Profile("Profile", Icons.Filled.Person, Icons.Outlined.Person),
    CompanyProfile("Company"),
    WalletDetails("Wallet"),
    Create("Create")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoute() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val authViewModel: com.jason.alp_vp.viewmodel.AuthViewModel = viewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // Check login status on start
    LaunchedEffect(Unit) {
        authViewModel.checkLoginStatus()
    }

    // Pages that should show the bottom bar
    val rootPages = listOf(AppView.Forum.name, AppView.Posts.name, AppView.Events.name, AppView.Profile.name)

    // Auth pages (no top/bottom bar)
    val authPages = listOf(AppView.Login.name, AppView.Register.name)

    // Determine if we can navigate back (not on root pages)
    val canNavigateBack = currentRoute !in rootPages &&
                         currentRoute !in authPages &&
                         navController.previousBackStackEntry != null

    Scaffold(
        topBar = {
            val noHeaderPages = listOf(AppView.PostDetail.name) + authPages
            val currentBaseRoute = currentRoute?.split("/")?.first()
            if (currentBaseRoute != null && currentBaseRoute !in noHeaderPages) {
                val displayView = AppView.entries.find { it.name == currentBaseRoute } ?: AppView.Forum
                MyTopAppBar(displayView, navController, canNavigateBack)
            }
        },
        bottomBar = {
            if (currentRoute in rootPages) {
                val items = listOf(
                    BottomNavItem(AppView.Forum, "Home"),
                    BottomNavItem(AppView.Posts, "Posts"),
                    BottomNavItem(AppView.Events, "Events"),
                    BottomNavItem(AppView.Profile, "Profile")
                )
                MyBottomNavigationBar(navController, navBackStackEntry?.destination, items)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) AppView.Forum.name else AppView.Login.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Auth Routes
            composable(AppView.Login.name) {
                LoginScreen(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }

            composable(AppView.Register.name) {
                RegisterScreen(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }

            composable(AppView.Forum.name) {
                HomePage(
                    onNavigateToBountyDetail = { bountyId ->
                        navController.navigate("bounty_detail/$bountyId")
                    }
                )
            }

            composable(AppView.Posts.name) {
                ForumPage(
                    onNavigateToEventPage = { navController.navigate(AppView.Events.name) },
                    onNavigateToPostPage = { /* Already on posts */ },
                    onNavigateToPostDetail = { postId ->
                        navController.navigate("${AppView.PostDetail.name}/$postId")
                    }
                )
            }

            composable("${AppView.PostDetail.name}/{postId}") { backStackEntry ->
                val postId = backStackEntry.arguments?.getString("postId")?.toIntOrNull() ?: -1
                val vm: ForumPageViewModel = viewModel()

                LaunchedEffect(postId) {
                    if (postId != -1) vm.selectPost(postId)
                }

                val selectedPost by vm.selectedPost.collectAsState()
                val replies by vm.selectedPostReplies.collectAsState()
                val timeAgo by vm.selectedPostTimeAgo.collectAsState()

                if (selectedPost != null) {
                    PostDetail(
                        post = selectedPost!!,
                        replies = replies,
                        timeAgo = timeAgo,
                        onUpvotePost = { vm.upvotePost(it) },
                        onDownvotePost = { vm.downvotePost(it) },
                        onUpvoteReply = { vm.upvoteReply(it) },
                        onDownvoteReply = { vm.downvoteReply(it) },
                        onSendReply = { vm.sendReply(it) }
                    )
                } else {
                    // Loading state
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }

            composable(AppView.Events.name) {
                EventPage(onBack = { navController.popBackStack() })
            }

            composable(AppView.Profile.name) {
                ProfileScreen(
                    authViewModel = authViewModel,
                    onNavigateToLogin = {
                        navController.navigate(AppView.Login.name) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigateToEditProfile = {
                        navController.navigate("profile_edit")
                    }
                )
            }

            composable(AppView.CompanyProfile.name) {
                CompanyProfileView(
                    onNavigateToWalletDetails = { navController.navigate(AppView.WalletDetails.name) },
                    onBountyClick = {},
                    onLogout = {}
                )
            }

            composable(AppView.WalletDetails.name) {
                WalletDetailsView(onNavigateBack = { navController.popBackStack() }, onAddPaymentMethod = {})
            }

            composable(AppView.Create.name) {
                CreatePage(onNavigateBack = { navController.popBackStack() })
            }

            // Bounty Detail Route
            composable("bounty_detail/{bountyId}") { backStackEntry ->
                val bountyId = backStackEntry.arguments?.getString("bountyId") ?: ""
                com.jason.alp_vp.ui.screens.BountyDetailScreen(
                    bountyId = bountyId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Profile Edit Route
            composable("profile_edit") {
                com.jason.alp_vp.ui.screens.ProfileEditScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun MyBottomNavigationBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    items: List<BottomNavItem>
) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.view.name } == true
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.view.name) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(item.label, fontSize = 10.sp) },
                icon = {
                    val icon = if (isSelected) item.view.selectedIcon else item.view.unselectedIcon
                    if (icon != null) Icon(icon, contentDescription = null)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF9333EA),
                    indicatorColor = Color(0xFFF3E8FF),
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    currentView: AppView,
    navController: NavHostController,
    canNavigateBack: Boolean
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = currentView.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black
        )
    )
}
