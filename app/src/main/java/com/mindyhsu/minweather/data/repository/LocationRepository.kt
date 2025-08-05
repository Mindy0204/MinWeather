package com.mindyhsu.minweather.data.repository

import android.location.Location
import com.mindyhsu.minweather.model.Result

interface LocationRepository {
    suspend fun getLastLocation(): Result<Location>
}