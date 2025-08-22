package com.mindyhsu.minweather.feature.search

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.mindyhsu.minweather.R
import com.mindyhsu.minweather.model.geo.LocationInfo
import com.mindyhsu.minweather.ui.EmptyState
import com.mindyhsu.minweather.ui.LoadingOverlay
import com.mindyhsu.minweather.utils.emptyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchQuery: String,
    searchResults: List<LocationInfo>,
    recentLocations: List<LocationInfo>,
    isSearching: Boolean,
    onSearchTextChange: (String) -> Unit,
    onExitSearchMode: () -> Unit,
    onSearchResultClick: (LocationInfo) -> Unit
) {
    var isExpanded by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    // Automatically focus on the input field and show the keyboard when this screen is first composed.
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .focusRequester(focusRequester),
            inputField = {
                InputField(
                    query = searchQuery,
                    onQueryChange = onSearchTextChange,
                    onSearch = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it },
                    placeholder = {
                        Text(text = stringResource(id = R.string.search_placeholder))
                    },
                    leadingIcon = {
                        IconButton(
                            onClick = {
                                onExitSearchMode()
                                backDispatcher?.onBackPressed()
                            }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    onSearchTextChange(emptyString())
                                    focusRequester.requestFocus()
                                    keyboardController?.show()
                                }) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    }
                )
            },
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            content = {
                if (isSearching) {
                    LoadingOverlay()
                } else {
                    val itemModifier =
                        Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.spacing_l))
                    val callback: (LocationInfo) -> Unit = { location ->
                        onSearchResultClick(location)
                    }

                    if (searchQuery.isBlank()) {
                        SearchItems(
                            modifier = itemModifier,
                            items = recentLocations,
                            onItemClick = callback
                        )
                    } else if (searchResults.isNotEmpty()) {
                        SearchItems(
                            modifier = itemModifier,
                            items = searchResults,
                            onItemClick = callback
                        )
                    } else {
                        EmptyState(message = stringResource(R.string.search_result_empty))
                    }
                }
            }
        )
    }

    // OnBackPressedDispatcher handles back in LIFO: the most recently registered enabled callback runs first
    BackHandler {
        onExitSearchMode()
    }
}