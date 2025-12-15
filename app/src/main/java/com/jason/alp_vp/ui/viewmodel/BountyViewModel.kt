package com.jason.alp_vp.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.jason.alp_vp.ui.model.Bounty


class BountyViewModel : ViewModel() {

    var activeBounties by mutableStateOf(listOf<Bounty>())
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
    }

    fun submitBounty(id: String) {
        activeBounties = activeBounties.filterNot { it.id == id }
    }

    fun canTakeMore(): Boolean = activeBounties.size < 3
}
