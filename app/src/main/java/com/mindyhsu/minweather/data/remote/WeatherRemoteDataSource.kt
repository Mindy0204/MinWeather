package com.mindyhsu.minweather.data.remote

import com.mindyhsu.minweather.model.weather.CurrentWeather
import com.mindyhsu.minweather.model.weather.Forecast

interface WeatherRemoteDataSource {
    suspend fun fetchCurrentWeather(latitude: String, longitude: String): CurrentWeather

    suspend fun fetch5DaysForecast(latitude: String, longitude: String): Forecast
}