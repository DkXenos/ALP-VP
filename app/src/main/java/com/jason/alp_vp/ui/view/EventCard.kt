package com.jason.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jason.alp_vp.ui.model.Event
import com.jason.alp_vp.ui.viewmodel.ForumPageViewModel

// Colors (local to EventCard)
private val CardBackground = Color(0xFF14161A)
private val TimeBadge = Color(0xFF413847)
private val RegisterGreen = Color(0xFF55D07E)
private val TitleColor = Color(0xFFFFFFFF)
private val SubText = Color(0xFF98A0B3)

@Composable
fun EventCard(
    event: Event,
    modifier: Modifier = Modifier,
    viewModel: ForumPageViewModel = viewModel(),
    onRegister: (Int) -> Unit = {}
) {
    val timeText = viewModel.formatDurationShort(event.eventDate)
    val registeredCount = event.registrations.size

    Card(
        colors = CardDefaults.cardColors(CardBackground),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    // Left tag
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color(0xFF0F2B7F), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text("EVENT", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Time badge top-right
                    Box(
                        modifier = Modifier
                            .background(TimeBadge, shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(timeText, color = Color(0xFFFFE9E9), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title
                Text(
                    text = event.title,
                    color = TitleColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Byline
                Text(text = "by Company #${event.companyId}", color = SubText, fontSize = 13.sp)

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = event.description,
                    color = SubText,
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "$registeredCount/${event.registeredQuota} registered",
                        color = SubText,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { onRegister(event.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = RegisterGreen),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Text("Register Now", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun EventCardPreview() {
    val now = java.time.Instant.now()
    val sampleEvent = Event(
        id = 1,
        title = "Build Weekend Hackathon",
        description = "Join us to build cool projects and win prizes.",
        eventDate = now.plusSeconds(60 * 60 * 24),
        companyId = 7,
        registeredQuota = 200,
        registrations = emptyList()
    )

    EventCard(event = sampleEvent)
}
