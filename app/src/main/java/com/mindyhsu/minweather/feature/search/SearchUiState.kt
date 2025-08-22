package com.mindyhsu.minweather.feature.search

import com.mindyhsu.minweather.model.geo.LocationInfo
import com.mindyhsu.minweather.utils.emptyString

data class SearchUiState(
    val query: String = emptyString(),
    val isSearching: Boolean = false,
    val results: List<LocationInfo> = emptyList(),
    val recent: List<LocationInfo> = emptyList()
)
