package com.jason.alp_vp.data.repository

import android.util.Log
import com.jason.alp_vp.data.service.CreateEventRequest
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
        val wrapper = response.body() ?: throw IllegalStateException("Empty event response")
        val item = wrapper.data  // Unwrap from { "data": {...} }
        return dtoToUi(item)
    }

    suspend fun getAllEvents(): List<Event> {
        val response = service.getAllEvents()
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            Log.e("EventRepository", "Failed to fetch events: ${response.code()} - $msg")
            throw Exception("Failed to fetch events: ${response.code()} - $msg")
        }
        val wrapper = response.body()
        if (wrapper == null) {
            Log.e("EventRepository", "Response body is null")
            throw Exception("Empty response from server")
        }
        val items = wrapper.data  // Unwrap from { "data": [...] }
        Log.d("EventRepository", "Received ${items.size} events from API")
        return items.map { dtoToUi(it) }
    }

    suspend fun getEventById(id: Int): Event {
        val response = service.getEventById(id)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to fetch event: ${response.code()} - $msg")
        }
        val wrapper = response.body() ?: throw IllegalStateException("Empty event response")
        val item = wrapper.data  // Unwrap from { "data": {...} }
        return dtoToUi(item)
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
        val wrapper = response.body() ?: throw IllegalStateException("Empty event response")
        val item = wrapper.data  // Unwrap from { "data": {...} }
        return dtoToUi(item)
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
        val wrapper = response.body() ?: throw IllegalStateException("Empty response")
        val items = wrapper.data  // Unwrap from { "data": [...] }
        return items.map { dtoToUi(it) }
    }

    suspend fun registerToEvent(eventId: Int, userId: Int) {
        Log.d("EventRepository", "registerToEvent called with eventId=$eventId, userId=$userId")
        Log.d("EventRepository", "eventId type: ${eventId::class.simpleName}, userId type: ${userId::class.simpleName}")

        val req = EventRegistrationRequest(eventId = eventId, userId = userId)
        Log.d("EventRepository", "Request object created: eventId=${req.eventId}, userId=${req.userId}")

        val response = service.registerToEvent(req)
        Log.d("EventRepository", "Response code: ${response.code()}")

        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            Log.e("EventRepository", "Registration failed: $msg")
            throw Exception("Failed to register to event: ${response.code()} - $msg")
        }
        Log.d("EventRepository", "Successfully registered to event")
        // Response contains updated event data wrapped in { "data": {...} }
    }

    suspend fun unregisterFromEvent(eventId: Int, userId: Int) {
        val response = service.unregisterFromEvent(eventId, userId)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to unregister from event: ${response.code()} - $msg")
        }
    }


    private fun dtoToUi(item: EventResponseItem): Event {
        val eventDate = try {
            Instant.parse(item.event_date)
        } catch (e: Exception) {
            Log.e("EventRepository", "Failed to parse event_date: ${item.event_date} for event ${item.id}", e)
            // Try to parse as different formats
            try {
                // Try ISO format with timezone: 2026-01-15T10:00:00Z
                Instant.parse(item.event_date + "Z")
            } catch (e2: Exception) {
                Log.e("EventRepository", "Failed to parse with Z suffix, using far future date", e2)
                // Use a far future date instead of now() to avoid showing "ended"
                Instant.parse("2099-12-31T23:59:59Z")
            }
        }

        val createdAt = try {
            Instant.parse(item.created_at)
        } catch (e: Exception) {
            Log.w("EventRepository", "Failed to parse created_at: ${item.created_at}, using now()", e)
            Instant.now()
        }

        Log.d("EventRepository", "Parsed event ${item.id}: date=${eventDate}, now=${Instant.now()}, future=${eventDate.isAfter(Instant.now())}")

        return Event(
            id = item.id,
            title = item.title,
            description = item.description,
            eventDate = eventDate,
            companyId = item.company_id,
            companyName = item.company_name,  // Flattened from nested company.name
            registeredQuota = item.registered_quota,
            currentRegistrations = item.current_registrations,
            createdAt = createdAt
        )
    }
}
