package com.jason.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
    bountyId: String,
    onSubmit: () -> Unit
) {
    val bounties by viewModel.bounties.collectAsState()
    val bounty = bounties.find { it.id == bountyId } ?: return

    BountyDetailContent(
        bounty = bounty,
        onSubmit = onSubmit
    )
}

@Composable
fun BountyDetailContent(
    bounty: Bounty,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBG2)
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0xFF1E1E1E))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {  }
                )
                Text(
                    text = bounty.title,
                    color = PureWhite2,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

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
            onClick = onSubmit,
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
    BountyDetailContent(
        bounty = preview,
        onSubmit = {}
    )
}
