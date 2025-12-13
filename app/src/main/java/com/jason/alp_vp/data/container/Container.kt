package com.jason.alp_vp.data.container

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherContainer {

    companion object{
        val BASE_URL = "https://api.openweathermap.org"
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .baseUrl(BASE_URL)
        .build()

//    private val retrofitService: WeatherService by lazy {
//        retrofit.create(WeatherService::class.java)
//    }
//
//    public val weatherRepository: WeatherRepository by lazy {
//        WeatherRepository(retrofitService)
//    }
}