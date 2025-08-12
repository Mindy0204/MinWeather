package com.mindyhsu.minweather.data.remote

import com.mindyhsu.minweather.model.weather.CurrentWeather
import com.mindyhsu.minweather.model.weather.Forecast
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val apiService: WeatherApiService
) : WeatherRemoteDataSource {

    override suspend fun fetchCurrentWeather(latitude: String, longitude: String): CurrentWeather {
        return apiService.getCurrentWeather(latitude, longitude)
    }

    override suspend fun fetch5DaysForecast(latitude: String, longitude: String): Forecast {
        return apiService.get5DaysForecast(latitude, longitude)
    }
}