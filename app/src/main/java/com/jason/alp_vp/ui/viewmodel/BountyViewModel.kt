package com.jason.alp_vp.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.jason.alp_vp.ui.model.Bounty
import com.jason.alp_vp.ui.model.Event

class BountyViewModel : ViewModel() {

    var activeBounties by mutableStateOf(listOf<Bounty>())
        private set

    var events by mutableStateOf(listOf<Event>())
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
            Event("1", "AI Seminar", "Dec 2025", "Seminar about AI Trends"),
            Event("2", "Startup Bootcamp", "Jan 2026", "Learn about startups"),
        )
    }

    fun submitBounty(id: String) {
        activeBounties = activeBounties.filterNot { it.id == id }
    }

    fun canTakeMore(): Boolean = activeBounties.size < 3
}
