package com.jason.alp_vp.data.service

import com.google.gson.annotations.SerializedName
import com.jason.alp_vp.data.dto.event.EventResponseItem
import retrofit2.Response
import retrofit2.http.*

interface EventService {

    @POST("events")
    suspend fun createEvent(
        @Body event: CreateEventRequest
    ): Response<ApiResponseWrapper<EventResponseItem>>

    @GET("events")
    suspend fun getAllEvents(): Response<ApiResponseWrapper<List<EventResponseItem>>>

    @GET("events/{id}")
    suspend fun getEventById(
        @Path("id") id: Int
    ): Response<ApiResponseWrapper<EventResponseItem>>

    @PUT("events/{id}")
    suspend fun updateEvent(
        @Path("id") id: Int,
        @Body event: UpdateEventRequest
    ): Response<ApiResponseWrapper<EventResponseItem>>

    @DELETE("events/{id}")
    suspend fun deleteEvent(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("companies/{companyId}/events")
    suspend fun getEventsByCompany(
        @Path("companyId") companyId: String
    ): Response<ApiResponseWrapper<List<EventResponseItem>>>

    @POST("events/register")
    suspend fun registerToEvent(
        @Body registration: EventRegistrationRequest
    ): Response<ApiResponseWrapper<EventResponseItem>>

    @DELETE("events/{eventId}/users/{userId}")
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
    @SerializedName("event_id") val eventId: Int,
    @SerializedName("user_id") val userId: Int
)
