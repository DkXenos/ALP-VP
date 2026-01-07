package com.jason.alp_vp.data.repository

import com.jason.alp_vp.data.service.CreateEventRequest
import com.jason.alp_vp.data.dto.event.EventResponse
import com.jason.alp_vp.data.dto.event.EventResponseItem
import com.jason.alp_vp.data.service.EventRegistrationRequest
import com.jason.alp_vp.data.service.EventService
import com.jason.alp_vp.data.service.UpdateEventRequest
import com.jason.alp_vp.ui.model.Event
import java.time.Instant

class EventRepository(private val service: EventService) {

    suspend fun createEvent(title: String, description: String, eventDateIso: String, companyId: String, registeredQuota: Int): Event {
        val req = CreateEventRequest(
            title = title,
            description = description,
            event_date = eventDateIso,
            company_id = companyId,
            registered_quota = registeredQuota
        )
        val response = service.createEvent(req)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to create event: ${response.code()} - $msg")
        }
        val body = response.body()!!
        val items = flattenEventResponses(listOf(body))
        val first = items.firstOrNull() ?: throw IllegalStateException("Empty event response")
        return dtoToUi(first)
    }

    suspend fun getAllEvents(): List<Event> {
        val response = service.getAllEvents()
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to fetch events: ${response.code()} - $msg")
        }
        val body = response.body()!!
        val items = flattenEventResponses(body)
        return items.map { dtoToUi(it) }
    }

    suspend fun getEventById(id: Int): Event {
        val response = service.getEventById(id)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to fetch event: ${response.code()} - $msg")
        }
        val body = response.body()!!
        val items = flattenEventResponses(listOf(body))
        val first = items.firstOrNull() ?: throw IllegalStateException("Empty event response")
        return dtoToUi(first)
    }

    suspend fun updateEvent(id: Int, title: String? = null, description: String? = null, eventDateIso: String? = null, registeredQuota: Int? = null): Event {
        val req = UpdateEventRequest(
            title = title,
            description = description,
            event_date = eventDateIso,
            registered_quota = registeredQuota
        )
        val response = service.updateEvent(id, req)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to update event: ${response.code()} - $msg")
        }
        val body = response.body()!!
        val items = flattenEventResponses(listOf(body))
        val first = items.firstOrNull() ?: throw IllegalStateException("Empty event response")
        return dtoToUi(first)
    }

    suspend fun deleteEvent(id: Int) {
        val response = service.deleteEvent(id)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to delete event: ${response.code()} - $msg")
        }
    }

    suspend fun getEventsByCompany(companyId: String): List<Event> {
        val response = service.getEventsByCompany(companyId)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to fetch company events: ${response.code()} - $msg")
        }
        val body = response.body()!!
        val items = flattenEventResponses(body)
        return items.map { dtoToUi(it) }
    }

    suspend fun registerToEvent(eventId: Int, userId: Int) {
        val req = EventRegistrationRequest(eventId = eventId, userId = userId)
        val response = service.registerToEvent(req)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to register to event: ${response.code()} - $msg")
        }
        // body may contain registered user data; not required for repository at this time
    }

    suspend fun unregisterFromEvent(eventId: Int, userId: Int) {
        val response = service.unregisterFromEvent(eventId, userId)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to unregister from event: ${response.code()} - $msg")
        }
    }

    private fun flattenEventResponses(listOfResponses: List<EventResponse>): List<EventResponseItem> {
        val out = mutableListOf<EventResponseItem>()
        for (r in listOfResponses) {
            out.addAll(r)
        }
        return out
    }

    private fun dtoToUi(item: EventResponseItem): Event {
        return Event(
            id = item.id,
            title = item.title,
            description = item.description,
            eventDate = try { Instant.parse(item.event_date) } catch (_: Exception) { Instant.now() },
            companyId = item.company_id,
            companyName = item.company_name,  // Flattened from nested company.name
            registeredQuota = item.registered_quota,
            currentRegistrations = item.current_registrations,
            createdAt = try { Instant.parse(item.created_at) } catch (_: Exception) { Instant.now() }
        )
    }
}
