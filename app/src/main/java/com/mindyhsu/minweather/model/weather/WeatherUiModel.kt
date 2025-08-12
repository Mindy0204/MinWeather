package com.mindyhsu.minweather.model.weather

import com.mindyhsu.minweather.common.DateTimeConstants.DATE_FORMAT
import com.mindyhsu.minweather.common.DateTimeConstants.DAY_OF_WEEK_FORMAT
import com.mindyhsu.minweather.common.DateTimeConstants.TIME_FORMAT_AM_PM
import com.mindyhsu.minweather.utils.toUrl
import com.mindyhsu.minweather.utils.toCelsiusString
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class WeatherUiModel(
    val currentWeather: CurrentWeather,
    val forecast: Forecast
) {
    private val zoneOffset: ZoneOffset by lazy { ZoneOffset.ofTotalSeconds(currentWeather.timezone) }

    private fun epochToDateTime(second: Long): LocalDateTime =
        Instant.ofEpochSecond(second).atOffset(zoneOffset).toLocalDateTime()

    fun getNow() = epochToDateTime(currentWeather.dt)

    fun getSunriseDateTime() = epochToDateTime(currentWeather.sys.sunrise)

    fun getSunsetDateTime() = epochToDateTime(currentWeather.sys.sunset)

    fun getIconUrl() = currentWeather.weather.firstOrNull()?.icon?.toUrl().orEmpty()

    fun getNext24HForecast(): List<Forecast24HUiModel> {
        val now = getNow()
        val end = now.plusDays(1)

        return forecast.list
            .map {
                it to epochToDateTime(it.dt)
            }.takeWhile { (_, dt) ->
                dt.isBefore(end)
            }.map { (item, dt) ->
                Forecast24HUiModel(
                    timeStr = dt.format(DateTimeFormatter.ofPattern(TIME_FORMAT_AM_PM)),
                    icon = item.weather.firstOrNull()?.icon?.toUrl().orEmpty(),
                    tempStr = item.main.temp.toCelsiusString()
                )
            }
    }

    fun getNext5DForecast(): List<Forecast5DUiModel> {
        val today = getNow().toLocalDate()
        return forecast.list
            .map {
                it to epochToDateTime(it.dt)
            }.distinctBy { (_, dt) ->
                dt.toLocalDate()
            }.filter { (_, dt) ->
                dt.toLocalDate().isAfter(today)
            }.map { (item, dt) ->
                Forecast5DUiModel(
                    dateStr = dt.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                    dayOfWeek = dt.format(DateTimeFormatter.ofPattern(DAY_OF_WEEK_FORMAT)),
                    icon = item.weather.firstOrNull()?.icon?.toUrl().orEmpty(),
                    tempStr = item.main.temp.toCelsiusString(),
                    minTempStr = item.main.tempMin.toCelsiusString(),
                    maxTempStr = item.main.tempMax.toCelsiusString()
                )
            }
    }
}
