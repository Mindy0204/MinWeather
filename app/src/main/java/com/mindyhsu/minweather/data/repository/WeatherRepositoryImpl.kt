package com.mindyhsu.minweather.data.repository

import com.mindyhsu.minweather.data.remote.WeatherRemoteDataSource
import com.mindyhsu.minweather.model.Result
import com.mindyhsu.minweather.model.weather.CurrentWeather
import com.mindyhsu.minweather.model.weather.Forecast
import timber.log.Timber
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {

    override suspend fun getCurrentWeather(
        latitude: String,
        longitude: String
    ): Result<CurrentWeather> {
        return try {
            val weather = remoteDataSource.fetchCurrentWeather(latitude, longitude)
            Result.Success(weather)
        } catch (e: Exception) {
            Timber.d("[WeatherRepositoryImpl] getCurrentWeather from remote error: $e")
            Result.Error(e)
        }
    }

    override suspend fun get5DaysForecast(latitude: String, longitude: String): Result<Forecast> {
        return try {
            val forecast = remoteDataSource.fetch5DaysForecast(latitude, longitude)
            Result.Success(forecast)
        } catch (e: Exception) {
            Timber.d("[WeatherRepositoryImpl] get5DaysForecast from remote error: $e")
            Result.Error(e)
        }
    }
}