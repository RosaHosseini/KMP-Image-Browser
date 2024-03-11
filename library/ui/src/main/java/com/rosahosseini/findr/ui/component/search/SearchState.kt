package com.rosahosseini.findr.ui.component.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.rosahosseini.findr.ui.extensions.doOncePerInstance

@Composable
fun rememberSearchState(initialQuery: String, onQueryChange: (String) -> Unit): SearchState {
    return remember { SearchState(initialQuery) }.also { state ->
        state.termTextField.text.trim().lowercase().doOncePerInstance(block = onQueryChange)
    }
}

class SearchState internal constructor(initialQueryText: String) {
    var termTextField by mutableStateOf(TextFieldValue(initialQueryText))
    var focused by mutableStateOf(false)
    val isBackEnabled: Boolean
        get() = focused || termTextField.text.isNotEmpty()

    val searchDisplay: SearchDisplay
        get() = if (focused) SearchDisplay.SUGGESTIONS else SearchDisplay.RESULTS
}

/**
 * Enum class with different values to set search state based on text, focus, initial state and
 * results from search.
 */
enum class SearchDisplay {
    SUGGESTIONS,
    RESULTS
}
