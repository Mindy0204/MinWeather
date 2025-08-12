package com.mindyhsu.minweather.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double, // probability of precipitation
    val rain: RainAndSnow3H? = null,
    val sys: Pod, // Part of the Day
    @SerialName("dt_txt") val dtTxt: String
)
