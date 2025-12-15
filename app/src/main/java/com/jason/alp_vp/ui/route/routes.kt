package com.jason.alp_vp.ui.route

sealed class Screen(val route: String) {
    object Forum : Screen("forum")
    object PostList : Screen("posts")
    object PostDetail : Screen("post/{postId}") {
        fun createRoute(postId: Int) = "post/$postId"
    }
    object Events : Screen("events")
    object CompanyProfile : Screen("company")
    object WalletDetails : Screen("wallet")
}
