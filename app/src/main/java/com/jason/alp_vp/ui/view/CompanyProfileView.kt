package com.jason.alp_vp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.ui.view.components.*
import com.jason.alp_vp.ui.viewmodel.CompanyProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyProfileView(
    onNavigateToWalletDetails: () -> Unit = {},
    onNavigateToFollowers: () -> Unit = {},
    onNavigateToFollowing: () -> Unit = {},
    onNavigateToFollowedPages: () -> Unit = {},
    onBountyClick: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: CompanyProfileViewModel = viewModel()
) {
    val companyProfile by viewModel.companyProfile.collectAsState()
    val walletBalance by viewModel.walletBalance.collectAsState()
    val activeBounties by viewModel.activeBounties.collectAsState()
    val expiredBounties by viewModel.expiredBounties.collectAsState()
    val followersCount by viewModel.followersCount.collectAsState()
    val followingCount by viewModel.followingCount.collectAsState()
    val followedPagesCount by viewModel.followedPagesCount.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF0A0B0F)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {

            Spacer(modifier = Modifier.height(16.dp))

            companyProfile?.let { company ->
                CompanyProfileHeader(
                    companyName = company.name,
                    walletBalance = walletBalance,
                    onLogout = {
                        viewModel.logout()
                        onLogout()
                    }
                )

                AboutUsCard(description = company.description)

                CompanySocialStats(
                    followersCount = followersCount,
                    followingCount = followingCount,
                    followedPagesCount = followedPagesCount,
                    onFollowersClick = onNavigateToFollowers,
                    onFollowingClick = onNavigateToFollowing,
                    onFollowedPagesClick = onNavigateToFollowedPages
                )

                ActiveBountiesSection(
                    activeBounties = activeBounties,
                    onBountyClick = { bounty -> onBountyClick(bounty.id) }
                )

                ExpiredBountiesSection(
                    expiredBounties = expiredBounties,
                    onBountyClick = { bounty -> onBountyClick(bounty.id) }
                )

                WalletQuickAccess(
                    currentBalance = walletBalance,
                    onViewWalletDetails = onNavigateToWalletDetails
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        if (loadingState) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        errorState?.let { err ->
            LaunchedEffect(err) {
                viewModel.clearError()
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun CompanyProfileViewPreview() {
    CompanyProfileView()
}
