package com.jason.alp_vp.ui.model

import com.jason.alp_vp.data.api.model.BountyResponse

data class Bounty(
    val id: String,
    val title: String,
    val company: String,
    val deadline: String,
    val rewardXp: Int,
    val rewardMoney: Int,
    val status: String,
    val description: String? = null,
    val requirements: List<String>? = null
)

/**
 * Extension function to convert API response to UI model
 */
fun BountyResponse.toBounty(): Bounty {
    return Bounty(
        id = this.id,
        title = this.title,
        company = this.company,
        deadline = this.deadline,
        rewardXp = this.rewardXp,
        rewardMoney = this.rewardMoney,
        status = this.status,
        description = this.description,
        requirements = this.requirements
    )
}

