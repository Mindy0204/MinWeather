package com.mindyhsu.minweather.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class Forecast24HUiModel(
    val timeStr: String,
    val icon: String,
    val tempStr: String
)
