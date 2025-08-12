package com.mindyhsu.minweather.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RainAndSnow3H(
    @SerialName("3h") val threeHour: Double? = null
)
