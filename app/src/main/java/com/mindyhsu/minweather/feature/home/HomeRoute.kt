package com.mindyhsu.minweather.feature.home

import android.Manifest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    // --- Permissions ---
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(permissionState.permissions.map { it.status }) {
        viewModel.handlePermissionResult(permissionState.permissions)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                permissionState.launchMultiplePermissionRequest()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // --- State ---
    // TODO: Replace isSearchMode flag with proper Navigation when multi-screen is introduced.
    // TODO: Give SearchScreen / WeatherScreen their own scoped ViewModels OR make them stateless.
    val isSearchMode by viewModel.isSearchMode.collectAsState()
    val weatherUiState by viewModel.weatherUiState.collectAsState()
    val searchUiState by viewModel.searchUiState.collectAsState()

    HomeScreen(
        modifier = Modifier.fillMaxSize(),
        isSearchMode = isSearchMode,
        weatherUiState = weatherUiState,
        onFabClick = viewModel::enterSearchMode,
        searchUiState = searchUiState,
        onSearchTextChange = viewModel::onSearchTextChange,
        onExitSearchMode = viewModel::exitSearchMode,
        onSearchResultClick = viewModel::onResultClick
    )
}