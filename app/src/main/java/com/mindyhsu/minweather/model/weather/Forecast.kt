package com.mindyhsu.minweather.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Forecast(
    val cod: String,
    val message: Int,
    @SerialName("cnt") val numTimestamp: Int,
    val list: List<ForecastItem>,
    val city: City
)
