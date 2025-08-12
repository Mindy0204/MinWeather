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
import com.mindyhsu.minweather.model.weather.WeatherUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _locationDesRes = MutableStateFlow<Int?>(null)
    val locationDesRes = _locationDesRes.asStateFlow()

    private val _weatherUiModel = MutableStateFlow<WeatherUiModel?>(null)
    val weatherUiModel = _weatherUiModel.asStateFlow()

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
                    loadWeatherData(
                        result.data.latitude.toString(),
                        result.data.longitude.toString()
                    )
                }

                is Result.Error -> {
                    Timber.d("[WeatherViewModel] getCurrentLocation result error")
                }

                is Result.Fail -> {
                    Timber.d("[WeatherViewModel] getCurrentLocation result fail")
                }

                is Result.Loading -> {
                    Timber.d("[WeatherViewModel] getCurrentLocation loading")
                }
            }
        }
    }

    private fun loadWeatherData(latitude: String, longitude: String) {
        viewModelScope.launch {
            val currentWeather = weatherRepository.getCurrentWeather(latitude, longitude)
            val forecast = weatherRepository.get5DaysForecast(latitude, longitude)

            if (currentWeather is Result.Success && forecast is Result.Success) {
                _weatherUiModel.update {
                    WeatherUiModel(currentWeather.data, forecast.data)
                }
            } else {
                Timber.d("[WeatherViewModel] loadWeatherData result error")
            }
        }
    }
}