package com.mindyhsu.minweather.ui

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindyhsu.minweather.R
import com.mindyhsu.minweather.utils.emptyString
import com.mindyhsu.minweather.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val recentLocations by viewModel.recentLocations.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    var isExpanded by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = searchQuery,
                onQueryChange = { viewModel.onSearchTextChange(it) },
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
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable {
                            viewModel.exitSearchMode()
                            backDispatcher?.onBackPressed()
                        }
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear",
                            modifier = Modifier.clickable {
                                viewModel.onSearchTextChange(emptyString())
                                focusRequester.requestFocus()
                                keyboardController?.show()
                            }
                        )
                    }
                }
            )
        },
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        content = {
            if (isSearching) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                SearchItems(
                    items = if (searchQuery.isNotBlank()) searchResults else recentLocations,
                    onItemClick = { location -> viewModel.onResultClick(location) }
                )
            }
        }
    )

    // OnBackPressedDispatcher handles back in LIFO: the most recently registered enabled callback runs first
    BackHandler {
        viewModel.exitSearchMode()
    }
}