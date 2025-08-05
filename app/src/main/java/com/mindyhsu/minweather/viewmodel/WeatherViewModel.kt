package com.mindyhsu.minweather.viewmodel

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.mindyhsu.minweather.R
import com.mindyhsu.minweather.data.repository.LocationRepository
import com.mindyhsu.minweather.data.repository.WeatherRepository
import com.mindyhsu.minweather.model.Result
import com.mindyhsu.minweather.model.weather.CurrentWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _locationDesRes = MutableStateFlow<Int?>(null)
    val locationDesRes = _locationDesRes.asStateFlow()

    private val _currentWeather = MutableStateFlow(CurrentWeather())
    val currentWeather = _currentWeather.asStateFlow()

    @OptIn(ExperimentalPermissionsApi::class)
    fun handlePermissionResult(permissions: List<PermissionState>) {
        val coarseGranted = permissions.find {
            it.permission == Manifest.permission.ACCESS_COARSE_LOCATION
        }?.status?.isGranted == true
        val fineGranted = permissions.find {
            it.permission == Manifest.permission.ACCESS_FINE_LOCATION
        }?.status?.isGranted == true

        if (coarseGranted || fineGranted) {
            getCurrentLocation()
        } else {
            _locationDesRes.update {
                R.string.location_status_unable
            }
        }
    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            when (val result = locationRepository.getLastLocation()) {
                is Result.Success -> {
                    getCurrentWeather(
                        result.data.latitude.toString(),
                        result.data.longitude.toString()
                    )
                }

                is Result.Error -> TODO()
                is Result.Fail -> TODO()
                Result.Loading -> TODO()
            }
        }
    }

    private fun getCurrentWeather(latitude: String, longitude: String) {
        viewModelScope.launch {
            when (val result = weatherRepository.getCurrentWeather(latitude, longitude)) {
                is Result.Success -> {
                    _currentWeather.update {
                        result.data
                    }
                }

                is Result.Error -> TODO()
                is Result.Fail -> TODO()
                Result.Loading -> TODO()
            }
        }
    }
}