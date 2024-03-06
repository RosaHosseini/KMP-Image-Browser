package com.rosahosseini.findr.feature.search.view.components

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.rosahosseini.findr.ui.component.search.SearchBar
import com.rosahosseini.findr.ui.component.search.SearchState
import com.rosahosseini.findr.ui.component.search.rememberSearchState
import com.rosahosseini.findr.ui.theme.FindrTheme

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
internal fun SearchBarComponent(
    state: SearchState,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val dispatcher: OnBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!state.focused) {
                    isEnabled = false
                    dispatcher.onBackPressed()
                } else {
                    state.termTextField = TextFieldValue("")
                    state.focused = false
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            }
        }
    }

    DisposableEffect(dispatcher) { // dispose/relaunch if dispatcher changes
        dispatcher.addCallback(backCallback)
        onDispose {
            backCallback.remove() // avoid leaks!
        }
    }

    SearchBar(
        query = state.termTextField,
        onQueryChange = { state.termTextField = it },
        onSearchFocusChange = { state.focused = it },
        onBack = { state.termTextField = TextFieldValue("") },
        focused = state.focused,
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

@Preview
@Composable
private fun EmptySearchBarComponentPreview() {
    FindrTheme {
        SearchBarComponent(
            state = rememberSearchState(initialQuery = "", onQueryChange = {})
        )
    }
}

@Preview
@Composable
private fun SearchBarComponentPreview() {
    FindrTheme {
        SearchBarComponent(
            state = rememberSearchState(initialQuery = "term", onQueryChange = {})
        )
    }
}
