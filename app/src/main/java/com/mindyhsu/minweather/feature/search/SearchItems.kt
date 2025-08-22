package com.mindyhsu.minweather.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.mindyhsu.minweather.R
import com.mindyhsu.minweather.model.geo.LocationInfo

@Composable
fun SearchItems(
    modifier: Modifier = Modifier,
    items: List<LocationInfo>,
    onItemClick: (LocationInfo) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_s))
    ) {
        items(items) { location ->
            Text(
                text = location.displayName(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.spacing_xs))
                    .clickable { onItemClick(location) },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}