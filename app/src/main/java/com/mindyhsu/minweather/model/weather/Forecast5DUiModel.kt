package com.mindyhsu.minweather.model.weather

import kotlinx.serialization.Serializable

@Serializable
data class Forecast5DUiModel(
    val dateStr: String,
    val dayOfWeek: String,
    val icon: String,
    val tempStr: String,
    val minTempStr: String,
    val maxTempStr: String
)
