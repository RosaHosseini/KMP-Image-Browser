package com.rosahosseini.findr.ui.component.search

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.library.ui.R
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrTheme

@ExperimentalAnimationApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBarComponent(
    state: SearchState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onSearchClick: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    BackHandler(state.focused) {
        focusManager.clearFocus()
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(visible = state.isBackEnabled) {
            // Back button
            IconButton(
                onClick = {
                    focusManager.clearFocus()
                    state.termTextField = TextFieldValue()
                }
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = backgroundColor
                )
            }
        }
        SearchTextField(
            query = state.termTextField,
            onQueryChange = { state.termTextField = it },
            onSearchFocusChange = {
                state.focused = it
                if (!it) keyboardController?.hide()
            },
            onSearch = {
                focusManager.clearFocus()
                onSearchClick()
            },
            modifier = Modifier
                .weight(1f)
                .padding(
                    top = Dimensions.medium,
                    bottom = Dimensions.medium,
                    start = if (!state.isBackEnabled) Dimensions.large else 0.dp
                ),
            backgroundColor = backgroundColor,
            contentColor = contentColor
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
private fun EmptySearchBarComponentPreview() {
    FindrTheme {
        SearchBarComponent(
            state = SearchState(initialQueryText = ""),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
private fun SearchBarComponentPreview() {
    FindrTheme {
        SearchBarComponent(
            state = SearchState(initialQueryText = "term"),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
