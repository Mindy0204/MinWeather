package com.mindyhsu.minweather.model.geo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationInfo(
    val name: String,
    @SerialName("local_names") val localNames: Map<String, String>? = null,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String? = null
) {
    fun displayName() = listOfNotNull(name, state, country).joinToString(", ")
}
