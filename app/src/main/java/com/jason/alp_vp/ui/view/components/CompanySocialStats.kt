package com.jason.alp_vp.ui.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CompanySocialStats(
    followersCount: Int,
    followingCount: Int,
    followedPagesCount: Int,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
    onFollowedPagesClick: () -> Unit,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SocialStatItem(
                count = followersCount,
                label = "Followers",
                onClick = onFollowersClick,
                modifier = Modifier.weight(1f)
            )

            SocialStatItem(
                count = followingCount,
                label = "Following",
                onClick = onFollowingClick,
                modifier = Modifier.weight(1f)
            )

            SocialStatItem(
                count = followedPagesCount,
                label = "Pages",
                onClick = onFollowedPagesClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SocialStatItem(
    count: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = NumberFormat.getNumberInstance(Locale.getDefault()).format(count),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = Color(0xFF98A0B3),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF0A0B0F)
@Composable
fun CompanySocialStatsPreview() {
    CompanySocialStats(
        followersCount = 1247,
        followingCount = 89,
        followedPagesCount = 23,
        onFollowersClick = {},
        onFollowingClick = {},
        onFollowedPagesClick = {}
    )
}
