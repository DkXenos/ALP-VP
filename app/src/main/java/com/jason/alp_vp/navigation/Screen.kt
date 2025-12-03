package com.jason.alp_vp.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Bounties : Screen("bounties")
    object BountyDetails : Screen("bounty_details/{bountyId}") {
        fun createRoute(bountyId: String) = "bounty_details/$bountyId"
    }
    object BountyApply : Screen("bounty_apply/{bountyId}") {
        fun createRoute(bountyId: String) = "bounty_apply/$bountyId"
    }
    object BountySubmission : Screen("bounty_submission/{bountyId}") {
        fun createRoute(bountyId: String) = "bounty_submission/$bountyId"
    }
    object UserRegistered : Screen("user_registered/{bountyId}") {
        fun createRoute(bountyId: String) = "user_registered/$bountyId"
    }
    object Forums : Screen("forums")
    object Profile : Screen("profile")
}

