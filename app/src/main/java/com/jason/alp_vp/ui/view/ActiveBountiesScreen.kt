package com.jason.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

private val DarkBG = Color(0xFF0E0E0E)
private val CardDark = Color(0xFF1A1A1A)
private val PureWhite = Color(0xFFFFFFFF)
private val SoftGray = Color(0xFF9E9E9E)
private val PrimaryGreen = Color(0xFF00C155)
private val BottomNavDark = Color(0xFF111111)

@Suppress("unused")
@Composable
fun ActiveBountiesScreen(
    viewModel: BountyViewModel
) {
    ActiveBountiesContent(
        bounties = viewModel.activeBounties,
        onView = { /* no navigation here (static preview safe) */ }
    )
}

@Composable
fun ActiveBountiesContent(
    bounties: List<Bounty>,
    onView: (Bounty) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBG)
            .padding(16.dp)
    ) {
        Text(
            text = "Active Bounties",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PureWhite
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "${bounties.size} / 3 Active",
            fontSize = 14.sp,
            color = PrimaryGreen
        )

        Spacer(Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(bounties) { bounty ->
                BountyCard(bounty, onView)
            }
        }

        BottomNavigationBar()
    }
}

@Composable
fun BountyCard(bounty: Bounty, onView: (Bounty) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardDark, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text(
            text = bounty.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = PureWhite
        )

        Spacer(Modifier.height(4.dp))

        Text(text = bounty.company, fontSize = 14.sp, color = SoftGray)

        Spacer(Modifier.height(6.dp))

        Text(text = "Deadline: ${bounty.deadline}", fontSize = 12.sp, color = SoftGray)

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { onView(bounty) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) {
            Text("View", color = PureWhite)
        }
    }
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(BottomNavDark),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Bounties", color = PrimaryGreen)
        Text("Events", color = SoftGray)
        Text("Profile", color = SoftGray)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewActiveBounties() {
    val sample = remember {
        listOf(
            Bounty("1", "Build Website", "BlueTech", "31 Dec 2025", 300, 500000, "active"),
            Bounty("2", "UI Redesign", "Aura Corp", "15 Jan 2026", 200, 350000, "active"),
        )
    }

    ActiveBountiesContent(bounties = sample, onView = {})
}
