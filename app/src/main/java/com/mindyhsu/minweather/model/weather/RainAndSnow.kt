package com.mindyhsu.minweather.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RainAndSnow(
    @SerialName("1h") val oneHour: Double? = null
)
