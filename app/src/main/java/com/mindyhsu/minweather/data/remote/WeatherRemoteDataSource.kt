package com.mindyhsu.minweather.data.remote

import com.mindyhsu.minweather.model.weather.CurrentWeather

interface WeatherRemoteDataSource {
    suspend fun fetchCurrentWeather(latitude: String, longitude: String): CurrentWeather
}