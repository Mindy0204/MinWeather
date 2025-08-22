package com.mindyhsu.minweather.utils

import com.mindyhsu.minweather.common.NetworkConstants.WEATHER_ICON_URL
import com.mindyhsu.minweather.common.TemperatureConstants.TEMP_K_OFFSET
import kotlin.math.roundToInt

fun Double.toCelsiusString() = (this - TEMP_K_OFFSET).roundToInt().toString()

fun Double.toPercentage(decimalPlaces: Int = 0) = "%.${decimalPlaces}f%%".format(this * 100)

fun String.toUrl() = if (this.isNotBlank()) String.format(WEATHER_ICON_URL, this) else emptyString()

fun emptyString() = ""