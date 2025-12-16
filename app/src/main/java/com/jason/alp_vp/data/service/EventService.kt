package com.jason.alp_vp.data.service

import com.jason.alp_vp.data.dto.event.EventResponse
import retrofit2.Response
import retrofit2.http.*

interface EventService {

    @POST("/events")
    suspend fun createEvent(
        @Body event: CreateEventRequest
    ): Response<EventResponse>

    @GET("/events")
    suspend fun getAllEvents(): Response<List<EventResponse>>

    @GET("/events/{id}")
    suspend fun getEventById(
        @Path("id") id: Int
    ): Response<EventResponse>

    @PUT("/events/{id}")
    suspend fun updateEvent(
        @Path("id") id: Int,
        @Body event: UpdateEventRequest
    ): Response<EventResponse>

    @DELETE("/events/{id}")
    suspend fun deleteEvent(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("/companies/{companyId}/events")
    suspend fun getEventsByCompany(
        @Path("companyId") companyId: String
    ): Response<List<EventResponse>>

    @POST("/events/register")
    suspend fun registerToEvent(
        @Body registration: EventRegistrationRequest
    ): Response<EventResponse>

    @DELETE("/events/{eventId}/users/{userId}")
    suspend fun unregisterFromEvent(
        @Path("eventId") eventId: Int,
        @Path("userId") userId: Int
    ): Response<Unit>
}

data class CreateEventRequest(
    val title: String,
    val description: String,
    val event_date: String, // ISO date string
    val company_id: String,
    val registered_quota: Int
)

data class UpdateEventRequest(
    val title: String? = null,
    val description: String? = null,
    val event_date: String? = null,
    val registered_quota: Int? = null
)

data class EventRegistrationRequest(
    val eventId: Int,
    val userId: Int
)
