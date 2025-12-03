package com.jason.alp_vp.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Bounties : Screen("bounties")
    object Active : Screen("active")
    object BountyDetails : Screen("bounty_details/{bountyId}") {
        fun createRoute(bountyId: String) = "bounty_details/$bountyId"
    }
    object ActiveBountyDetails : Screen("active_bounty_details/{bountyId}") {
        fun createRoute(bountyId: String) = "active_bounty_details/$bountyId"
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
    object PostDetails : Screen("post_details/{postId}") {
        fun createRoute(postId: String) = "post_details/$postId"
    }
    object PortfolioDetails : Screen("portfolio_details/{portfolioIndex}") {
        fun createRoute(portfolioIndex: Int) = "portfolio_details/$portfolioIndex"
    }
    object AddPortfolio : Screen("add_portfolio")
    object CreateBounty : Screen("create_bounty")
    object ApplicantDetails : Screen("applicant_details/{applicantId}") {
        fun createRoute(applicantId: String) = "applicant_details/$applicantId"
    }
}

