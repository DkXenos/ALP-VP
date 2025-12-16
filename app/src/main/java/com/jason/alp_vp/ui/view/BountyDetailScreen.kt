package com.jason.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jason.alp_vp.ui.model.Bounty
import com.jason.alp_vp.ui.viewmodel.BountyViewModel

private val DarkBG2 = Color(0xFF0E0E0E)
private val PureWhite2 = Color(0xFFFFFFFF)
private val PrimaryGreen2 = Color(0xFF00C155)
private val SoftGray2 = Color(0xFFBBBBBB)

@Suppress("unused")
@Composable
fun BountyDetailScreen(
    viewModel: BountyViewModel,
    bountyId: String
) {
    val bounties by viewModel.bounties.collectAsState()
    val bounty = bounties.find { it.id == bountyId } ?: return
    BountyDetailContent(bounty)
}

@Composable
fun BountyDetailContent(bounty: Bounty) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBG2)
            .padding(20.dp)
    ) {
        Text(
            text = bounty.title,
            color = PureWhite2,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(Modifier.height(12.dp))

        Text(text = "Company: ${bounty.company}", color = PureWhite2, fontSize = 16.sp)

        Spacer(Modifier.height(10.dp))

        Text(text = "Deadline: ${bounty.deadline}", color = SoftGray2, fontSize = 14.sp)

        Spacer(Modifier.height(20.dp))

        Text(text = "Rewards", color = PureWhite2, fontSize = 18.sp, fontWeight = FontWeight.Medium)

        Spacer(Modifier.height(6.dp))

        Text(text = "XP: ${bounty.rewardXp}", color = SoftGray2, fontSize = 14.sp)
        Text(text = "Money: ${bounty.rewardMoney}", color = SoftGray2, fontSize = 14.sp)

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = { /* no nav */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen2)
        ) {
            Text("Submit Work", color = PureWhite2)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDetailScreen() {
    val preview = remember {
        Bounty(
            id = "1",
            title = "Build App UI",
            company = "Design Corp",
            deadline = "10 Feb 2026",
            rewardXp = 150,
            rewardMoney = 250000,
            status = "active"
        )
    }
    BountyDetailContent(preview)
}
