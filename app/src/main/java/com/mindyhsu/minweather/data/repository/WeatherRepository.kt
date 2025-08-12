package com.mindyhsu.minweather.data.repository

import com.mindyhsu.minweather.model.Result
import com.mindyhsu.minweather.model.weather.CurrentWeather
import com.mindyhsu.minweather.model.weather.Forecast

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude: String, longitude: String): Result<CurrentWeather>

    suspend fun get5DaysForecast(latitude: String, longitude: String): Result<Forecast>
}