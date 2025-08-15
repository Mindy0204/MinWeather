package com.mindyhsu.minweather.ui

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mindyhsu.minweather.R
import com.mindyhsu.minweather.common.DateTimeConstants.DATETIME_FORMAT_FULL
import com.mindyhsu.minweather.common.DateTimeConstants.TIME_FORMAT
import com.mindyhsu.minweather.model.weather.WeatherUiModel
import com.mindyhsu.minweather.utils.toCelsiusString
import java.time.format.DateTimeFormatter

@Composable
fun WeatherScreen(
    modifier: Modifier,
    data: WeatherUiModel
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Location & DateTime
        item {
            Text(
                text = stringResource(
                    id = R.string.two_parameters_with_comma,
                    data.currentWeather.name,
                    data.currentWeather.sys.country
                ), fontSize = 28.sp, fontWeight = FontWeight.Bold
            )
            Text(
                text = data.getNow().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT_FULL)),
                fontSize = 16.sp,
                color = Color.Gray
            )

        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Weather Summary
        item {
            AsyncImage(
                model = data.getIconUrl(),
                contentDescription = data.currentWeather.weather.firstOrNull()?.description,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = data.currentWeather.weather.firstOrNull()?.description
                    ?: stringResource(id = R.string.placeholder),
                fontSize = 22.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Temp
        item {
            Text(
                text = stringResource(
                    id = R.string.temp_with_feels_like,
                    data.currentWeather.main.temp.toCelsiusString(),
                    data.currentWeather.main.feelsLike.toCelsiusString()
                ), fontSize = 22.sp, fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(
                    id = R.string.temp_high_low,
                    data.currentWeather.main.tempMax.toCelsiusString(),
                    data.currentWeather.main.tempMin.toCelsiusString()
                ), fontSize = 16.sp, color = Color.Gray
            )
        }

        // Humidity & Probability of Precipitation
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.humidity),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = data.currentWeather.main.humidity.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.pop),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = data.forecast.list.first().pop.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
        }

        // Sunrise & Sunset - Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.sunrise_and_sunset),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(
                            id = R.string.two_parameters_with_slash,
                            data.getSunriseDateTime()
                                .format(DateTimeFormatter.ofPattern(TIME_FORMAT)),
                            data.getSunsetDateTime()
                                .format(DateTimeFormatter.ofPattern(TIME_FORMAT))
                        ),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.End
                    )
                }
            }
        }

        item {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }

        // Next 24H Forecast (every 3 hours)
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(data.getNext24HForecast()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = it.timeStr)
                        AsyncImage(
                            model = it.icon,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(text = stringResource(id = R.string.temp_degree, it.tempStr))
                    }
                }
            }
        }

        item {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }

        // 5 Days Forecast - LazyColumn
        items(data.getNext5DForecast()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(
                        id = R.string.two_parameters_with_space,
                        it.dateStr,
                        it.dayOfWeek
                    ),
                    modifier = Modifier.weight(0.3f)
                )
                AsyncImage(
                    model = it.icon,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.temp_degree, it.tempStr),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(0.15f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(
                        id = R.string.temp_high_low, it.maxTempStr, it.minTempStr
                    ),
                    modifier = Modifier.weight(0.4f)
                )
            }
        }
    }
}
