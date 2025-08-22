package com.mindyhsu.minweather.feature.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.mindyhsu.minweather.R
import com.mindyhsu.minweather.common.DateTimeConstants
import com.mindyhsu.minweather.model.weather.Forecast24HUiModel
import com.mindyhsu.minweather.model.weather.WeatherUiModel
import com.mindyhsu.minweather.utils.toCelsiusString
import com.mindyhsu.minweather.utils.toPercentage
import java.time.format.DateTimeFormatter

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    data: WeatherUiModel
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = dimensionResource(R.dimen.spacing_l),
                start = dimensionResource(R.dimen.spacing_l),
                end = dimensionResource(R.dimen.spacing_l)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            LocationHeader(
                location = stringResource(
                    id = R.string.two_parameters_with_comma,
                    data.currentWeather.name,
                    data.currentWeather.sys.country
                ),
                dateTime = data.getNow()
                    .format(DateTimeFormatter.ofPattern(DateTimeConstants.DATETIME_FORMAT_FULL))
            )
        }

        item { Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_m))) }

        item {
            WeatherSummary(
                iconUrl = data.getIconUrl(),
                description = data.currentWeather.weather.firstOrNull()?.description
                    ?: stringResource(id = R.string.placeholder),
                temp = stringResource(
                    id = R.string.temp_with_feels_like,
                    data.currentWeather.main.temp.toCelsiusString(),
                    data.currentWeather.main.feelsLike.toCelsiusString()
                ),
                highLowTemp = stringResource(
                    id = R.string.temp_high_low,
                    data.currentWeather.main.tempMax.toCelsiusString(),
                    data.currentWeather.main.tempMin.toCelsiusString()
                )
            )
        }

        item { Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_m))) }

        // --- Humidity & Probability of Precipitation ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s))
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.humidity),
                    content = stringResource(
                        id = R.string.percentage,
                        data.currentWeather.main.humidity.toString()
                    )
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.pop),
                    content = data.forecast.list.first().pop.toPercentage()
                )
            }
        }

        item { Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_s))) }

        // --- Wind Speed & Visibility ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s))
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.wind_speed),
                    content = stringResource(
                        id = R.string.meter_per_sec,
                        data.currentWeather.wind.speed.toString()
                    )
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.visibility),
                    content = stringResource(
                        id = R.string.meter,
                        data.currentWeather.visibility.toString()
                    )
                )
            }
        }

        item { Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_s))) }

        item {
            val formatter = DateTimeFormatter.ofPattern(DateTimeConstants.TIME_FORMAT)

            SunriseSunsetCard(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.sunrise_and_sunset),
                content = stringResource(
                    id = R.string.two_parameters_with_slash,
                    data.getSunriseDateTime().format(formatter),
                    data.getSunsetDateTime().format(formatter)
                )
            )
        }

        item {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_m)),
                thickness = dimensionResource(R.dimen.divider_thickness),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

        // --- Next 24H Forecast (every 3 hours) ---
        item { HourlyForecastRow(items = data.getNext24HForecast()) }

        item {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_m)),
                thickness = dimensionResource(R.dimen.divider_thickness),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

        // --- 5 Days Forecast ---
        items(data.getNext5DForecast()) {
            DailyForecastRow(
                dateAndWeek = stringResource(
                    id = R.string.two_parameters_with_space,
                    it.dateStr,
                    it.dayOfWeek
                ),
                icon = it.icon,
                temp = stringResource(id = R.string.temp_degree, it.tempStr),
                highLow = stringResource(
                    id = R.string.temp_high_low, it.maxTempStr, it.minTempStr
                )
            )
        }
    }
}

@Composable
private fun LocationHeader(
    modifier: Modifier = Modifier,
    location: String,
    dateTime: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = location,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_s)))
        Text(
            text = dateTime,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun WeatherSummary(
    modifier: Modifier = Modifier,
    iconUrl: String,
    description: String,
    temp: String,
    highLowTemp: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier.size(dimensionResource(R.dimen.icon_size_large)),
            model = iconUrl,
            contentDescription = description,
            contentScale = ContentScale.Fit
        )
        Text(
            modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_s)),
            text = description,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_m)))

        Text(
            text = temp,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_s)))
        Text(
            text = highLowTemp,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    content: String
) {
    Card(
        modifier = modifier
            .fillMaxHeight(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.spacing_s)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_s)))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun SunriseSunsetCard(
    modifier: Modifier = Modifier,
    title: String,
    content: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_s)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun HourlyForecastRow(
    modifier: Modifier = Modifier,
    items: List<Forecast24HUiModel>
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s))
    ) {
        items(items) { it ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = it.timeStr,
                    style = MaterialTheme.typography.titleSmall
                )
                AsyncImage(
                    model = it.icon,
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size)),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = stringResource(id = R.string.temp_degree, it.tempStr),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun DailyForecastRow(
    modifier: Modifier = Modifier,
    dateAndWeek: String,
    icon: String,
    temp: String,
    highLow: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = dateAndWeek,
            modifier = Modifier.weight(0.3f),
            style = MaterialTheme.typography.titleMedium
        )
        AsyncImage(
            model = icon,
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(R.dimen.icon_size)),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.width(dimensionResource(R.dimen.spacing_s)))
        Text(
            text = temp,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(0.15f)
        )
        Spacer(Modifier.width(dimensionResource(R.dimen.spacing_s)))
        Text(
            text = highLow,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(0.4f)
        )
    }
}