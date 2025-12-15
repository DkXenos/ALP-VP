package com.jason.alp_vp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.ui.view.components.WithdrawSection
import com.jason.alp_vp.ui.view.components.PaymentMethodsCard
import com.jason.alp_vp.ui.view.components.WalletHistoryList
import com.jason.alp_vp.ui.viewmodel.CompanyProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletDetailsView(
    onNavigateBack: () -> Unit = {},
    onAddPaymentMethod: () -> Unit = {},
    viewModel: CompanyProfileViewModel = viewModel()
) {
    val walletBalance by viewModel.walletBalance.collectAsState()
    val walletHistory by viewModel.walletHistory.collectAsState()
    val paymentMethods by viewModel.paymentMethods.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Wallet Details",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A0B0F)
                )
            )
        },
        containerColor = Color(0xFF0A0B0F)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Wallet Balance Card (fully-qualified to avoid unresolved import issues)
            com.jason.alp_vp.ui.view.components.WalletBalanceCard(
                currentBalance = walletBalance
            )

            // Withdraw Money Section
            WithdrawSection(
                availableBalance = walletBalance,
                paymentMethods = paymentMethods,
                onWithdraw = { amount, paymentMethodId ->
                    viewModel.withdrawMoney(amount, paymentMethodId)
                },
                isLoading = loadingState
            )

            // Payment Methods Section
            PaymentMethodsCard(
                paymentMethods = paymentMethods,
                onAddPaymentMethod = onAddPaymentMethod,
                onRemovePaymentMethod = { /* Handle remove payment method */ }
            )

            // Transaction History Section
            WalletHistoryList(
                transactions = walletHistory
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Error Snackbar
        errorState?.let { error ->
            LaunchedEffect(error) {
                // Show snackbar for error
                viewModel.clearError()
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF0A0B0F)
@Composable
fun WalletDetailsViewPreview() {
    WalletDetailsView()
}
