package com.jason.alp_vp.ui.route

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
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
import com.jason.alp_vp.ui.view.*
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
    Forum("Forum", Icons.Filled.Home, Icons.Outlined.Home),
    Posts("Posts", Icons.AutoMirrored.Filled.List, Icons.AutoMirrored.Outlined.List),
    PostDetail("PostDetail"),
    Events("Events"),
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

    // Pages that should show the bottom bar
    val rootPages = listOf(AppView.Forum.name, AppView.Posts.name, AppView.Events.name)

    // Determine if we can navigate back (not on root pages)
    val canNavigateBack = currentRoute !in rootPages && navController.previousBackStackEntry != null

    Scaffold(
        topBar = {
            val noHeaderPages = listOf(AppView.PostDetail.name)
            val currentBaseRoute = currentRoute?.split("/")?.first()
            if (currentBaseRoute != null && currentBaseRoute !in noHeaderPages) {
                val displayView = AppView.entries.find { it.name == currentBaseRoute } ?: AppView.Forum
                MyTopAppBar(displayView, navController, canNavigateBack)
            }
        },
        bottomBar = {
            if (currentRoute in rootPages) {
                val items = listOf(
                    BottomNavItem(AppView.Forum, "Forum"),
                    BottomNavItem(AppView.Posts, "Posts"),
                    BottomNavItem(AppView.Events, "Events")
                )
                MyBottomNavigationBar(navController, navBackStackEntry?.destination, items)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppView.Forum.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppView.Forum.name) {
                ForumPage(
                    onNavigateToEventPage = { navController.navigate(AppView.Events.name) },
                    onNavigateToPostPage = { navController.navigate(AppView.Posts.name) },
                    onNavigateToPostDetail = { postId ->
                        navController.navigate("${AppView.PostDetail.name}/$postId")
                    }
                )
            }

            composable(AppView.Posts.name) {
                PostPage(
                    onBack = { navController.popBackStack() },
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

                if (selectedPost != null) {
                    PostDetail(
                        post = selectedPost!!,
                        replies = replies,
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
