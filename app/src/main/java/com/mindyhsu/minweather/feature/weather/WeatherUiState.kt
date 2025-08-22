package com.mindyhsu.minweather.feature.weather

import com.mindyhsu.minweather.model.weather.WeatherUiModel

data class WeatherUiState(
    val data: WeatherUiModel? = null,
    val locationDesRes: Int? = null,
    val isLoading: Boolean = false
)
