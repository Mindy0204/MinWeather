package com.mindyhsu.minweather.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)
