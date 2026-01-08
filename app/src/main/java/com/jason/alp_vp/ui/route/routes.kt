package com.jason.alp_vp.ui.route

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.jason.alp_vp.ui.theme.*
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
    Home("Home", Icons.Filled.Home, Icons.Outlined.Home),  // Homepage with all bounties
    Active("Active", Icons.Filled.Work, Icons.Outlined.Work),  // My active/claimed bounties
    Bounties("Bounties", Icons.Filled.Home, Icons.Outlined.Home),  // Legacy - redirect to Home
    Forum("Forums", Icons.AutoMirrored.Filled.List, Icons.AutoMirrored.Outlined.List),  // Events/Posts tabs
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
    val rootPages = listOf(AppView.Home.name, AppView.Active.name, AppView.Forum.name, AppView.Profile.name)

    // Auth pages (no top/bottom bar)
    val authPages = listOf(AppView.Login.name, AppView.Register.name)

    // Determine if we can navigate back (not on root pages)
    val canNavigateBack = currentRoute !in rootPages &&
                         currentRoute !in authPages &&
                         navController.previousBackStackEntry != null

    Scaffold(
        topBar = {
            val noHeaderPages = listOf(AppView.PostDetail.name) + authPages + rootPages  // Hide top bar on root pages too
            val currentBaseRoute = currentRoute?.split("/")?.first()

            // Show top bar for detail pages like bounty_detail, profile_edit
            if (currentBaseRoute != null && currentBaseRoute !in noHeaderPages) {
                val title = when (currentBaseRoute) {
                    "bounty_detail" -> "Bounty Details"
                    "profile_edit" -> "Edit Profile"
                    "event_detail" -> "Event Details"
                    else -> {
                        val displayView = AppView.entries.find { it.name == currentBaseRoute } ?: AppView.Forum
                        displayView.title
                    }
                }
                MyTopAppBar(title, navController, canNavigateBack)
            }
        },
        bottomBar = {
            if (currentRoute in rootPages) {
                val items = listOf(
                    BottomNavItem(AppView.Home, "Home"),
                    BottomNavItem(AppView.Active, "Active"),
                    BottomNavItem(AppView.Forum, "Forums"),
                    BottomNavItem(AppView.Profile, "Profile")
                )
                MyBottomNavigationBar(navController, navBackStackEntry?.destination, items)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) AppView.Home.name else AppView.Login.name,
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

            // Home Page (All bounties)
            composable(AppView.Home.name) {
                val bountyViewModel: com.jason.alp_vp.ui.viewmodel.BountyViewModel = viewModel()

                // Refresh bounties when navigating to this screen
                LaunchedEffect(currentRoute) {
                    if (currentRoute == AppView.Home.name) {
                        bountyViewModel.loadAllBounties()
                    }
                }

                HomePage(
                    bountyViewModel = bountyViewModel,
                    onNavigateToBountyDetail = { bountyId: String ->
                        navController.navigate("bounty_detail/$bountyId")
                    }
                )
            }

            // Active Page (My active/claimed bounties ONLY)
            composable(AppView.Active.name) {
                val profileViewModel: com.jason.alp_vp.ui.viewmodel.ProfileViewModel = viewModel()

                // Only refresh if we don't have data yet
                LaunchedEffect(Unit) {
                    profileViewModel.loadProfile()
                }

                ActiveBountiesScreen(
                    profileViewModel = profileViewModel,
                    onNavigateToBountyDetail = { bountyId: String ->
                        navController.navigate("bounty_detail/$bountyId")
                    },
                    onNavigateToSubmission = { bountyId: String ->
                        navController.navigate("submission/$bountyId")
                    }
                )
            }

            // Legacy Bounties route (redirect to Home)
            composable(AppView.Bounties.name) {
                val bountyViewModel: com.jason.alp_vp.ui.viewmodel.BountyViewModel = viewModel()

                LaunchedEffect(currentRoute) {
                    if (currentRoute == AppView.Bounties.name) {
                        bountyViewModel.loadAllBounties()
                    }
                }

                HomePage(
                    bountyViewModel = bountyViewModel,
                    onNavigateToBountyDetail = { bountyId: String ->
                        navController.navigate("bounty_detail/$bountyId")
                    }
                )
            }

            // Forum Page (Events and Posts with tabs)
            composable(AppView.Forum.name) {
                ForumPage(
                    onNavigateToEventPage = { navController.navigate(AppView.Events.name) },
                    onNavigateToPostPage = { /* Already on forum */ },
                    onNavigateToPostDetail = { postId ->
                        navController.navigate("${AppView.PostDetail.name}/$postId")
                    },
                    onNavigateToEventDetail = { eventId ->
                        navController.navigate("event_detail/$eventId")
                    }
                )
            }

            // Legacy Posts route (redirect to Forum)
            composable(AppView.Posts.name) {
                ForumPage(
                    onNavigateToEventPage = { navController.navigate(AppView.Events.name) },
                    onNavigateToPostPage = { /* Already on posts */ },
                    onNavigateToPostDetail = { postId ->
                        navController.navigate("${AppView.PostDetail.name}/$postId")
                    },
                    onNavigateToEventDetail = { eventId ->
                        navController.navigate("event_detail/$eventId")
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
                EventPage(
                    onBack = { navController.popBackStack() },
                    onNavigateToEventDetail = { eventId ->
                        navController.navigate("event_detail/$eventId")
                    }
                )
            }

            composable(AppView.Profile.name) {
                val profileViewModel: com.jason.alp_vp.ui.viewmodel.ProfileViewModel = viewModel()

                // Only refresh when first loading
                LaunchedEffect(Unit) {
                    profileViewModel.refresh()
                }

                ProfileScreen(
                    authViewModel = authViewModel,
                    profileViewModel = profileViewModel,
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
                val bountyViewModel: com.jason.alp_vp.ui.viewmodel.BountyViewModel = viewModel()
                val profileViewModel: com.jason.alp_vp.ui.viewmodel.ProfileViewModel = viewModel()

                com.jason.alp_vp.ui.screens.BountyDetailScreen(
                    bountyId = bountyId,
                    onNavigateBack = { navController.popBackStack() },
                    onBountyClaimed = {
                        // Refresh bounties and profile data after claiming
                        bountyViewModel.loadAllBounties()
                        bountyViewModel.loadMyBounties()
                        profileViewModel.refresh()
                    }
                )
            }

            // Profile Edit Route
            composable("profile_edit") {
                com.jason.alp_vp.ui.screens.ProfileEditScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Submission Route
            composable("submission/{bountyId}") { backStackEntry ->
                val bountyId = backStackEntry.arguments?.getString("bountyId") ?: ""
                SubmissionScreen(
                    bountyId = bountyId,
                    onDone = {
                        navController.popBackStack()
                    }
                )
            }

            // Event Detail Route
            composable("event_detail/{eventId}") { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId")?.toIntOrNull() ?: 0
                val forumViewModel: ForumPageViewModel = viewModel()

                com.jason.alp_vp.ui.screens.EventDetailScreen(
                    eventId = eventId,
                    onNavigateBack = { navController.popBackStack() },
                    onEventRegistered = {
                        // Refresh events after registration
                        forumViewModel.refreshData()
                    }
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
    NavigationBar(
        containerColor = SurfaceDarkElevated,
        tonalElevation = 12.dp
    ) {
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
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    icon = {
                        val icon = if (isSelected) item.view.selectedIcon else item.view.unselectedIcon
                        if (icon != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) {
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    AccentCyan.copy(alpha = 0.2f),
                                                    AccentPurple.copy(alpha = 0.2f)
                                                )
                                            )
                                        } else {
                                            Brush.linearGradient(
                                                colors = listOf(Color.Transparent, Color.Transparent)
                                            )
                                        }
                                    )
                                    .padding(6.dp)
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentCyan,
                        selectedTextColor = AccentCyan,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    title: String,
    navController: NavHostController,
    canNavigateBack: Boolean
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = AccentCyan
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = SurfaceDarkElevated,
            titleContentColor = TextPrimary
        )
    )
}
