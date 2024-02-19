package com.rosahosseini.findr.ui.component.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest

/**
 * Creates a [SearchState] that is remembered across compositions. Uses [LaunchedEffect]
 *  and [snapshotFlow] to set states of search and return result or error state.
 *
 *  * When search gets focus state goes into [SearchDisplay.SUGGESTIONS] which some suggestion
 *  can be displayed to user.
 *
 *  * Immediately after user starts typing [SearchState.searchInProgress] sets to `true`
 *  to not get results while recomposition happens.
 **
 * @param debounceMillis timeout before user finishes typing.
 *
 * @param onQueryChange this lambda is for getting results from db, REST api or a ViewModel.
 */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@Composable
fun rememberSearchState(
    initialQuery: String,
    debounceMillis: Long = 0,
    onQueryChange: (String) -> Unit
): SearchState {
    return remember { SearchState(initialQuery) }.also { state ->
        LaunchedEffect(key1 = Unit) {
            snapshotFlow { state.query }
                .distinctUntilChanged()
                .map { it.text.clean() }
                .filter { !state.sameAsPreviousQuery() }
                .map { queryText: String ->
                    state.searchInProgress = true
                    queryText
                }
                .debounce(debounceMillis)
                .mapLatest {
                    onQueryChange(it)
                }.collect {
                    state.searchInProgress = false
                }
        }
    }
}

class SearchState(initialQueryText: String) {
    var query by mutableStateOf(TextFieldValue(initialQueryText))
    var focused by mutableStateOf(false)
    private var previousQueryText by mutableStateOf("")

    /**
     * Check if a search is initiated. Search is initiated after a specific condition
     */
    var searchInProgress by mutableStateOf(false)

    val searchDisplay: SearchDisplay
        get() = when {
            focused && query.text.isEmpty() -> {
                previousQueryText = query.text.clean()
                SearchDisplay.SUGGESTIONS
            }
            searchInProgress -> SearchDisplay.SEARCH_IN_PROGRESS
            else -> {
                previousQueryText = query.text.clean()
                SearchDisplay.RESULTS
            }
        }

    fun sameAsPreviousQuery() = query.text == previousQueryText
}

private fun String.clean(): String = trim().lowercase()

/**
 * Enum class with different values to set search state based on text, focus, initial state and
 * results from search.
 */
enum class SearchDisplay {
    SUGGESTIONS,
    SEARCH_IN_PROGRESS,
    RESULTS
}
