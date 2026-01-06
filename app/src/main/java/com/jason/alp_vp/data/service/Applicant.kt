package com.jason.alp_vp.data.service

data class Applicant(
    val userId: Int,
    val username: String? = null,
    val email: String,
    val claimedAt: String,
    val submissionUrl: String? = null,
    val submissionNotes: String? = null,
    val submittedAt: String? = null,
    val isWinner: Boolean = false
)
