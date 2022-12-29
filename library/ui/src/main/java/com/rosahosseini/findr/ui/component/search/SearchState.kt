package com.rosahosseini.findr.ui.component.search

import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

/**
 * Creates a [SearchState] that is remembered across compositions. Uses [LaunchedEffect]
 *  and [snapshotFlow] to set states of search and return result or error state.
 *
 *  * When search gets focus state goes into [SearchDisplay.SUGGESTIONS] which some suggestion
 *  can be displayed to user.
 *
 *  * Immediately after user starts typing [SearchState.searchInProgress] sets to `true`
 *  to not get results while recomposition happens.
 *
 *  After [debounceMillis] has passed [SearchState.searching] is set to `true`, progress icon
 *   can be displayed here.
 *
 * @param debounceMillis timeout before user finishes typing. After this
 * timeout [SearchState.searching] is set to true.
 *
 * @param onQueryChange this lambda is for getting results from db, REST api or a ViewModel.
 */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@Composable
fun rememberSearchState(
    debounceMillis: Long = 0,
    onQueryChange: (String) -> Unit,
): SearchState {

    return remember { SearchState() }.also { state ->
        LaunchedEffect(key1 = Unit) {
            snapshotFlow { state.query }
                .distinctUntilChanged()
                .filter { !state.sameAsPreviousQuery() }
                .map { query: TextFieldValue ->
                    if (debounceMillis > 0) {
                        state.searching = false
                    }
                    state.searchInProgress = true
                    query
                }
                .debounce(debounceMillis)
                .mapLatest { query: TextFieldValue ->
                    state.searching = true
                    onQueryChange(query.text.trim())
                }.collect {
                    state.searchInProgress = false
                    state.searching = false
                }
        }
    }
}

class SearchState {
    var query by mutableStateOf(TextFieldValue())
    var focused by mutableStateOf(false)
    var previousQueryText = ""
        private set

    /**
     * Check if search initial conditions are met and a search operation is going on.
     * This flag is for showing progressbar.
     */
    var searching by mutableStateOf(false)

    /**
     * Check if a search is initiated. Search is initiated after a specific condition
     * If  debounce or delay before user stops typing is not needed it can be
     * set to value of [searching].
     */
    var searchInProgress = searching

    val searchDisplay: SearchDisplay
        get() = when {
            focused && query.text.isEmpty() -> {
                previousQueryText = query.text
                SearchDisplay.SUGGESTIONS
            }
            searchInProgress -> SearchDisplay.SEARCH_IN_PROGRESS
            else -> {
                previousQueryText = query.text
                SearchDisplay.RESULTS
            }
        }

    /**
     * Check if user is running same query as the previous one
     */
    fun sameAsPreviousQuery() = query.text.trim() == previousQueryText.trim()
}

/**
 * Enum class with different values to set search state based on text, focus, initial state and
 * results from search.
 */
enum class SearchDisplay {
    /**
     * represents the state where search Composable gained focus but query is empty.
     * */
    SUGGESTIONS,

    /**
     * represents initiation of search but not actively searching.
     * For instance search might require at least 3 letters or some specific condition.
     * After condition is passed
     */
    SEARCH_IN_PROGRESS,

    /**
     * represents the state after a successful search operation that returned non empty results
     */
    RESULTS
}