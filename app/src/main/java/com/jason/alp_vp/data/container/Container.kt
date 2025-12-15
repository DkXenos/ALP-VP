package com.jason.alp_vp.data.container

import com.google.gson.GsonBuilder
import com.jason.alp_vp.data.service.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {

    companion object {
        // TODO: Replace with your actual backend IP address
        const val BASE_URL = "http://192.168.68.105:3000"
    }

    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()
    }

    // Service instances
    val commentService: CommentService by lazy {
        retrofit.create(CommentService::class.java)
    }

    val companyService: CompanyService by lazy {
        retrofit.create(CompanyService::class.java)
    }

    val eventService: EventService by lazy {
        retrofit.create(EventService::class.java)
    }

    val postService: PostService by lazy {
        retrofit.create(PostService::class.java)
    }

    val voteService: VoteService by lazy {
        retrofit.create(VoteService::class.java)
    }
}