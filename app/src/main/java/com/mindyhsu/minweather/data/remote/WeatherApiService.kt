package com.mindyhsu.minweather.data.remote

import com.mindyhsu.minweather.model.weather.CurrentWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): CurrentWeather
}