package com.jason.alp_vp.model

data class Submission(
    val id: String,
    val bountyId: String,
    val talentId: String,
    val notes: String,
    val fileUrl: String = "",
    val status: SubmissionStatus
)

enum class SubmissionStatus {
    DRAFT,
    SUBMITTED,
    APPROVED,
    REJECTED
}

