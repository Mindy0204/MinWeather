package com.mindyhsu.minweather.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mindyhsu.minweather.model.geo.LocationInfo
import com.mindyhsu.minweather.feature.search.SearchScreen
import com.mindyhsu.minweather.feature.search.SearchUiState
import com.mindyhsu.minweather.feature.weather.WeatherScreen
import com.mindyhsu.minweather.feature.weather.WeatherUiState
import com.mindyhsu.minweather.ui.EmptyState
import com.mindyhsu.minweather.ui.LoadingOverlay

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    isSearchMode: Boolean,
    weatherUiState: WeatherUiState,
    onFabClick: () -> Unit,
    searchUiState: SearchUiState,
    onSearchTextChange: (String) -> Unit,
    onExitSearchMode: () -> Unit,
    onSearchResultClick: (LocationInfo) -> Unit
) {

    // Using Scaffold now to prepare for future layout expansion.
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        floatingActionButton = {
            AnimatedVisibility(visible = !isSearchMode && !weatherUiState.isLoading) {
                FloatingActionButton(onClick = onFabClick) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        }
    ) { innerPadding ->
        if (weatherUiState.isLoading) {
            LoadingOverlay()
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            ) {
                AnimatedContent(targetState = isSearchMode) { searchMode ->
                    if (searchMode) {
                        with(searchUiState) {
                            SearchScreen(
                                modifier = Modifier.fillMaxSize(),
                                searchQuery = query,
                                searchResults = results,
                                recentLocations = recent,
                                isSearching = isSearching,
                                onSearchTextChange = onSearchTextChange,
                                onExitSearchMode = onExitSearchMode,
                                onSearchResultClick = onSearchResultClick
                            )
                        }
                    } else {
                        when {
                            weatherUiState.data != null -> {
                                WeatherScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    data = weatherUiState.data
                                )
                            }

                            else -> {
                                EmptyState(
                                    message = weatherUiState.locationDesRes?.let {
                                        stringResource(id = it)
                                    }.orEmpty()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}