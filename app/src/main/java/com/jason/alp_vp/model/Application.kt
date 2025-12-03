package com.jason.alp_vp.model

data class Application(
    val id: String,
    val bountyId: String,
    val applicantId: String,
    val applicantName: String,
    val applicantLevel: Int,
    val applicantXp: Int,
    val coverLetter: String,
    val status: ApplicationStatus
)

enum class ApplicationStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}

