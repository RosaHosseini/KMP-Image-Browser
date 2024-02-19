package com.rosahosseini.findr.ui.component.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.library.ui.R
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrColor

@ExperimentalAnimationApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onBack: () -> Unit,
    focused: Boolean,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(visible = focused) {
            // Back button
            IconButton(
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onBack()
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
            onSearchFocusChange = onSearchFocusChange,
            focused = focused,
            modifier = Modifier.weight(1f),
            backgroundColor = backgroundColor,
            contentColor = contentColor
        )
    }
}

/**
 * This is a stateless TextField for searching with a Hint when query is empty,
 * and clear [IconButton] to clear query
 */
@Composable
fun SearchTextField(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    focused: Boolean,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    Surface(
        modifier = modifier.then(
            Modifier
                .height(56.dp)
                .padding(
                    top = Dimensions.defaultMargin,
                    bottom = Dimensions.defaultMargin,
                    start = if (!focused) Dimensions.defaultMarginDouble else 0.dp
                )
        ),
        color = backgroundColor,
        shape = RoundedCornerShape(percent = 50)
    ) {
        Box(contentAlignment = Alignment.CenterStart) {
            if (query.text.isEmpty()) SearchHint()

            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(contentColor),

                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .onFocusChanged {
                            onSearchFocusChange(it.isFocused)
                        }
                        .focusRequester(focusRequester)
                        .padding(
                            top = 10.dp,
                            bottom = 8.dp,
                            start = Dimensions.defaultMarginTriple,
                            end = Dimensions.defaultMargin
                        ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    cursorBrush = SolidColor(contentColor)
                )
            }
        }
    }
}

@Composable
private fun SearchHint() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = Dimensions.defaultMarginTriple, end = Dimensions.defaultMargin)
    ) {
        Text(
            color = FindrColor.Grey30,
            text = stringResource(R.string.search_hint)
        )
    }
}
