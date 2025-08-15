package com.mindyhsu.minweather.ui

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mindyhsu.minweather.utils.emptyString
import com.mindyhsu.minweather.viewmodel.WeatherViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: WeatherViewModel = hiltViewModel()
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
    val isSearchMode by viewModel.isSearchMode.collectAsState()
    val weatherUiModel by viewModel.weatherUiModel.collectAsState()
    val locationDesRes by viewModel.locationDesRes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Using Scaffold now to prepare for future layout expansion.
    Scaffold(
        floatingActionButton = {
            if (!isSearchMode && !isLoading) {
                FloatingActionButton(
                    onClick = { viewModel.enterSearchMode() }
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        }
    ) { innerPadding ->
        Box(
            Modifier.fillMaxSize()
        ) {
            // TODO: Give SearchScreen / WeatherScreen their own scoped ViewModels OR make them stateless.
            if (isSearchMode) {
                SearchScreen(viewModel = viewModel)
            } else {
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    weatherUiModel?.let {
                        WeatherScreen(
                            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                            data = it
                        )
                    } ?: run {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = locationDesRes?.let {
                                    stringResource(id = it)
                                } ?: run {
                                    emptyString()
                                },
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}