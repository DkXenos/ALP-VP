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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.time.Duration
import java.time.Instant
import java.util.Locale

@Composable
fun ActiveBountiesSection(
    activeBounties: List<`Bounty.kt`>,
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
                text = "Active Bounties & Events",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (activeBounties.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🎯", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "No active bounties at the moment", color = Color(0xFF98A0B3))
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(activeBounties) { bounty ->
                        BountyCard(bounty = bounty, onClick = { onBountyClick(bounty) })
                    }
                }
            }
        }
    }
}

@Composable
fun BountyCard(
    bounty: `Bounty.kt`,
    onClick: () -> Unit,
    isExpired: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isExpired) Color(0xFF1A1A1A) else Color(0xFF1E2328)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bounty.title,
                        color = if (isExpired) Color(0xFF666666) else Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = bounty.description,
                        color = if (isExpired) Color(0xFF666666) else Color(0xFF98A0B3),
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (isExpired) {
                    Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFF85C5C).copy(alpha = 0.2f)) {
                        Text(text = "EXPIRED", color = Color(0xFFF85C5C), modifier = Modifier.padding(6.dp), fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                if (bounty.reward > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💰")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Rp ${NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID")).format(bounty.reward)}", color = Color(0xFF57D06A), fontWeight = FontWeight.Bold)
                    }
                }

                bounty.deadline?.let { dl ->
                    val hoursLeft = Duration.between(Instant.now(), dl).toHours()
                    if (hoursLeft > 0) {
                        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF2B62FF).copy(alpha = 0.12f)) {
                            Text(text = "${if (hoursLeft < 24) "${hoursLeft}h left" else "${hoursLeft/24}d left"}", color = Color(0xFF2B62FF), modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF0A0B0F)
@Composable
fun ActiveBountiesPreview() {
    ActiveBountiesSection(activeBounties = emptyList(), onBountyClick = {})
}
