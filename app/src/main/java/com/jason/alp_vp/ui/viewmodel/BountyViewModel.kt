package com.jason.alp_vp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jason.alp_vp.ui.model.Bounty
import com.jason.alp_vp.ui.model.EventPost

class BountyViewModel : ViewModel() {

    var activeBounties by mutableStateOf(listOf<Bounty>())
        private set

    var events by mutableStateOf(listOf<EventPost>())
        private set

    init {
        // Dummy data
        activeBounties = listOf(
            Bounty(
                id = "1",
                title = "Build Website",
                company = "BlueTech",
                deadline = "31 Dec 2025",
                rewardXp = 300,
                rewardMoney = 500000,
                status = "active"
            ),
            Bounty(
                id = "2",
                title = "UI Redesign",
                company = "Aura Corp",
                deadline = "15 Jan 2026",
                rewardXp = 200,
                rewardMoney = 350000,
                status = "active"
            )
        )

        events = listOf(
            EventPost(
                id = "1",
                title = "AI Seminar",
                organizer = "Tech Institute",
                description = "Seminar about AI Trends",
                registered = 45,
                capacity = 100,
                badgeEmoji = "🤖"
            ),
            EventPost(
                id = "2",
                title = "Startup Bootcamp",
                organizer = "Innovation Hub",
                description = "Learn about startups",
                registered = 32,
                capacity = 50,
                badgeEmoji = "🚀"
            )
        )
    }

    fun submitBounty(id: String) {
        activeBounties = activeBounties.filterNot { it.id == id }
    }

    fun canTakeMore(): Boolean = activeBounties.size < 3
}
