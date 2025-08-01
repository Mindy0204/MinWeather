package com.mindyhsu.minweather.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class Coordinator(
    val lon: Double? = null,
    val lat: Double? = null
)
