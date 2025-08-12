package com.mindyhsu.minweather.data.remote

import com.mindyhsu.minweather.model.weather.CurrentWeather
import com.mindyhsu.minweather.model.weather.Forecast
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): CurrentWeather

    @GET("data/2.5/forecast")
    suspend fun get5DaysForecast(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): Forecast
}