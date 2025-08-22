package com.mindyhsu.minweather.feature.home

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
import com.mindyhsu.minweather.model.geo.LocationInfo
import com.mindyhsu.minweather.model.weather.WeatherUiModel
import com.mindyhsu.minweather.feature.search.SearchUiState
import com.mindyhsu.minweather.utils.emptyString
import com.mindyhsu.minweather.feature.weather.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _isSearchMode = MutableStateFlow(false)
    val isSearchMode = _isSearchMode.asStateFlow()

    // --- Weather ---
    private val _weatherUiModel = MutableStateFlow<WeatherUiModel?>(null)
    private val _locationDesRes = MutableStateFlow<Int?>(null)
    private val _isLoading = MutableStateFlow(false)

    val weatherUiState: StateFlow<WeatherUiState> =
        combine(
            _weatherUiModel,
            _locationDesRes,
            _isLoading
        ) { data, desRes, isLoading ->
            WeatherUiState(
                data = data,
                locationDesRes = desRes,
                isLoading = isLoading
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Keep active for 5s after last subscriber
            initialValue = WeatherUiState()
        )

    // --- Search ---
    private val _searchQuery = MutableStateFlow(emptyString())
    private val _isSearching = MutableStateFlow(false)
    private val _searchResults = MutableStateFlow<List<LocationInfo>>(emptyList())
    private val _recentLocations = MutableStateFlow<List<LocationInfo>>(emptyList())

    val searchUiState: StateFlow<SearchUiState> =
        combine(
            _searchQuery,
            _isSearching,
            _searchResults,
            _recentLocations
        ) { query, searching, results, recent ->
            SearchUiState(
                query = query,
                isSearching = searching,
                results = results,
                recent = recent
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Keep active for 5s after last subscriber
            initialValue = SearchUiState()
        )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val searchingFlow: Flow<String> =
        combine(_isSearchMode, _searchQuery) { inSearch, text -> inSearch to text }
            .flatMapLatest { (inSearch, text) ->
                if (!inSearch) emptyFlow()
                else flowOf(text)
                    .debounce(500L)
            }
            .distinctUntilChanged()

    init {
        viewModelScope.launch {
            searchingFlow
                .onEach { text ->
                    _isSearching.value = true

                    when (val result =
                        locationRepository.searchPlace(locationName = text, limit = 5)) {
                        is Result.Success -> {
                            _searchResults.update {
                                result.data
                            }
                        }

                        is Result.Error -> {
                            Timber.d("[WeatherViewModel] searchingFlow result error")
                        }

                        is Result.Fail -> {
                            Timber.d("[WeatherViewModel] searchingFlow result fail")
                        }

                        is Result.Loading -> {
                            Timber.d("[WeatherViewModel] searchingFlow loading")
                        }
                    }

                    _isSearching.value = false
                }
                .stateIn(viewModelScope)
        }
    }

    fun enterSearchMode() {
        _isSearchMode.value = true
        _searchQuery.value = emptyString()
        _searchResults.value = emptyList()
    }

    fun exitSearchMode() {
        _isSearchMode.value = false
        _searchQuery.value = emptyString()
        _searchResults.value = emptyList()
    }

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
            _locationDesRes.value = R.string.location_status_unable
        }
    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            _isLoading.value = true

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

            _isLoading.value = false
        }
    }

    private fun loadWeatherData(latitude: String, longitude: String) {
        viewModelScope.launch {
            _isLoading.value = true

            val currentWeather = weatherRepository.getCurrentWeather(latitude, longitude)
            val forecast = weatherRepository.get5DaysForecast(latitude, longitude)

            if (currentWeather is Result.Success && forecast is Result.Success) {
                _weatherUiModel.update {
                    WeatherUiModel(currentWeather.data, forecast.data)
                }
            } else {
                Timber.d("[WeatherViewModel] loadWeatherData result error")
            }

            _isLoading.value = false
        }
    }

    fun onSearchTextChange(text: String) {
        _searchQuery.value = text
        if (text.length < 3) {
            _searchResults.value = emptyList()
        }
    }

    fun onResultClick(item: LocationInfo) {
        addRecent(item)
        loadWeatherData(latitude = item.lat.toString(), longitude = item.lon.toString())
        exitSearchMode()
    }

    // TODO: Implement room database to store recent searches
    private fun addRecent(location: LocationInfo) {
        _recentLocations.update {
            val list = it.toMutableList()
            list.remove(location)
            list.add(0, location)
            list.take(5)
        }
    }
}