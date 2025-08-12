package com.mindyhsu.minweather.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeather(
    val coord: Coordinator,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val rain: RainAndSnow? = null,
    val snow: RainAndSnow? = null,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)
