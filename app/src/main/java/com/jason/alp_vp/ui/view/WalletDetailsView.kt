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
import com.jason.alp_vp.ui.view.components.WithdrawSection
import com.jason.alp_vp.ui.view.components.PaymentMethodsCard
import com.jason.alp_vp.ui.view.components.WalletBalanceCard
import com.jason.alp_vp.ui.view.components.WalletHistoryList
import com.jason.alp_vp.ui.viewmodel.CompanyProfileViewModel

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Wallet Balance Card (fully-qualified to avoid unresolved import issues)
        WalletBalanceCard(
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

            // Error message display
            errorState?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3D1A1A)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = error,
                        color = Color(0xFFFF5C5C),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF0A0B0F)
@Composable
fun WalletDetailsViewPreview() {
    WalletDetailsView()
}
