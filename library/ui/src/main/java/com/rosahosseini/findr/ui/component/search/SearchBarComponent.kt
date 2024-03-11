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
    query: TextFieldValue,
    focused: Boolean,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onSearchClick: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val isBackIconVisible = focused || query.text.isNotEmpty()

    BackHandler(focused) {
        focusManager.clearFocus()
        onBackClick()
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(visible = isBackIconVisible) {
            // Back button
            IconButton(
                onClick = {
                    focusManager.clearFocus()
                    onBackClick()
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
            query = query,
            onQueryChange = onQueryChange,
            onSearchFocusChange = {
                onSearchFocusChange(it)
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
                    start = if (!isBackIconVisible) Dimensions.large else 0.dp
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
            query = TextFieldValue(),
            onQueryChange = {},
            onSearchFocusChange = {},
            focused = false,
            onBackClick = {},
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
            query = TextFieldValue("term"),
            onQueryChange = {},
            onSearchFocusChange = {},
            focused = true,
            onBackClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
