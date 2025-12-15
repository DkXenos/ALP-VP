package com.jason.alp_vp.ui.model

import com.jason.alp_vp.data.dto.event.EventResponseItem
import java.time.Instant
import java.time.format.DateTimeFormatter

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val eventDate: Instant,
    val companyId: Int,
    val companyName: String = "",
    val registeredQuota: Int,
    val currentRegistrations: Int = 0,
    val createdAt: Instant = Instant.now()
)

// Extension function to convert DTO to Model
fun EventResponseItem.toModel() = Event(
    id = this.id,
    title = this.title,
    description = this.description,
    eventDate = try {
        Instant.parse(this.eventDate)
    } catch (e: Exception) {
        Instant.now()
    },
    companyId = this.companyId,
    companyName = this.company.name,
    registeredQuota = this.registeredQuota,
    currentRegistrations = this.currentRegistrations,
    createdAt = try {
        Instant.parse(this.createdAt)
    } catch (e: Exception) {
        Instant.now()
    }
)
