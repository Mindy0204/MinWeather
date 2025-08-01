package com.mindyhsu.minweather.ui.theme

import androidx.lifecycle.ViewModel
import com.mindyhsu.minweather.data.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

}