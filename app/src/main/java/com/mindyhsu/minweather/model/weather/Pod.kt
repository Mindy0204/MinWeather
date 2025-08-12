package com.mindyhsu.minweather.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class Pod(
    val pod: String // (n - night, d - day)
)
