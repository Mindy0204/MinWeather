package com.mindyhsu.minweather.data.repository

import com.mindyhsu.minweather.model.Result
import com.mindyhsu.minweather.model.weather.CurrentWeather

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude: String, longitude: String): Result<CurrentWeather>
}