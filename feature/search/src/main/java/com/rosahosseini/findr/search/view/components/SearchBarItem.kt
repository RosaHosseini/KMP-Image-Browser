package com.rosahosseini.findr.search.view.components

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import com.rosahosseini.findr.ui.component.search.SearchBar
import com.rosahosseini.findr.ui.component.search.SearchState
import com.rosahosseini.findr.ui.theme.FindrColor

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun SearchBarItem(
    modifier: Modifier,
    state: SearchState,
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
                    state.query = TextFieldValue("")
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
        query = state.query,
        onQueryChange = { state.query = it },
        onSearchFocusChange = { state.focused = it },
        onBack = { state.query = TextFieldValue("") },
        focused = state.focused,
        modifier = modifier,
        backgroundColor = FindrColor.LightBackground,
        contentColor = FindrColor.TextDark
    )
}
