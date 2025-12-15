package com.jason.alp_vp.data.repository

import com.jason.alp_vp.ui.model.Event
import com.jason.alp_vp.ui.model.EventRegistration
import java.time.Instant

/**
 * Simple in-memory repository for events and attendees. Designed for dummy/demo usage only.
 */
object EventRepository {
    private var eventIdCounter = 1
    private val events = mutableMapOf<Int, Event>()
    private val registrations = mutableMapOf<Int, MutableList<EventRegistration>>()

    init {
        // Seed sample events
        val event1 = Event(
            id = eventIdCounter++,
            title = "Tech Startup Hiring Event",
            description = "Join us for an exclusive hiring event! We're looking for talented developers, designers, and marketers. Register now for priority access to our bounties!",
            eventDate = Instant.now().plusSeconds(7980), // ~2h 13m from now
            companyId = 1,
            registeredQuota = 100,
            createdAt = Instant.now().minusSeconds(86400)
        )

        val event2 = Event(
            id = eventIdCounter++,
            title = "Freelancer Networking Session",
            description = "Connect with other freelancers, share tips, and learn about upcoming opportunities. Special guest speakers from top companies!",
            eventDate = Instant.now().plusSeconds(19740), // ~5h 29m from now
            companyId = 2,
            registeredQuota = 150,
            createdAt = Instant.now().minusSeconds(3600)
        )

        val event3 = Event(
            id = eventIdCounter++,
            title = "ALP Community Seminar",
            description = "Join us for a tech seminar about modern Android app architecture and MVVM patterns.",
            eventDate = Instant.now().plusSeconds(172800), // 48 hours from now
            companyId = 1,
            registeredQuota = 100,
            createdAt = Instant.now()
        )

        events[event1.id] = event1
        events[event2.id] = event2
        events[event3.id] = event3

        // Pre-seed some registrations
        registrations[event1.id] = mutableListOf(
            EventRegistration(userId = 1, eventId = event1.id),
            EventRegistration(userId = 2, eventId = event1.id)
        )
        registrations[event2.id] = mutableListOf(
            EventRegistration(userId = 3, eventId = event2.id)
        )
        registrations[event3.id] = mutableListOf()
    }

    fun getAllEvents(): List<Event> {
        return events.values.map { event ->
            val regs = registrations[event.id] ?: emptyList()
            event.copy(registrations = regs)
        }
    }

    fun getEvent(eventId: Int): Event? {
        val event = events[eventId] ?: return null
        val regs = registrations[eventId] ?: emptyList()
        return event.copy(registrations = regs)
    }

    fun register(eventId: Int, userId: Int): Boolean {
        val event = events[eventId] ?: return false
        val regs = registrations.getOrPut(eventId) { mutableListOf() }

        // Check if already registered
        if (regs.any { it.userId == userId }) return false

        // Check if quota is full
        if (regs.size >= event.registeredQuota) return false

        regs.add(EventRegistration(userId = userId, eventId = eventId))
        return true
    }

    fun unregister(eventId: Int, userId: Int): Boolean {
        val regs = registrations[eventId] ?: return false
        return regs.removeIf { it.userId == userId }
    }
}

