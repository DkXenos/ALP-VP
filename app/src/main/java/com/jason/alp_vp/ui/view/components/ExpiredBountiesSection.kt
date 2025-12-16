package com.jason.alp_vp.ui.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpiredBountiesSection(
    expiredBounties: List<`Bounty.kt`>,
    onBountyClick: (`Bounty.kt`) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14161A)),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Expired Bounties",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (expiredBounties.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📅", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "No expired bounties", color = Color(0xFF98A0B3))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Great work keeping up with deadlines!", color = Color(0xFF98A0B3))
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(expiredBounties) { bounty ->
                        BountyCard(bounty = bounty, onClick = { onBountyClick(bounty) }, isExpired = true)
                    }
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF0A0B0F)
@Composable
fun ExpiredBountiesPreview() {
    ExpiredBountiesSection(expiredBounties = emptyList(), onBountyClick = {})
}
