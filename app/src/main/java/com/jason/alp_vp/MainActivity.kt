package com.jason.alp_vp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jason.alp_vp.ui.route.Screen
import com.jason.alp_vp.ui.theme.ALPVPTheme
import com.jason.alp_vp.ui.view.*
import com.jason.alp_vp.ui.viewmodel.ForumPageViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ALPVPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Forum.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(Screen.Forum.route) {
            ForumPage(
                onNavigateToEventPage = { navController.navigate(Screen.Events.route) },
                onNavigateToPostPage = { navController.navigate(Screen.PostList.route) }
            )
        }

        composable(Screen.PostList.route) {
            PostPage(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.PostDetail.route,
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: -1
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

        composable(Screen.Events.route) {
            EventPage(onBack = { navController.popBackStack() })
        }

        composable(Screen.CompanyProfile.route) {
            com.jason.alp_vp.ui.view.CompanyProfileView(
                onNavigateToWalletDetails = { navController.navigate(Screen.WalletDetails.route) },
                onBountyClick = { /* Could navigate to bounty detail if implemented */ },
                onLogout = { /* handle logout */ }
            )
        }

        composable(Screen.WalletDetails.route) {
            WalletDetailsView(onNavigateBack = { navController.popBackStack() }, onAddPaymentMethod = { /* TODO */ })
        }

        composable(Screen.Create.route) {
            CreatePage(onNavigateBack = { navController.popBackStack() })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppNavHostPreview() {
    ALPVPTheme {
        AppNavHost()
    }
}