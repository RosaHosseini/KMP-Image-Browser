package com.rosahosseini.findr.ui.component.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.library.ui.Res
import com.rosahosseini.findr.library.ui.search_hint
import com.rosahosseini.findr.ui.theme.Dimensions
import org.jetbrains.compose.resources.stringResource

/**
 * This is a stateless TextField for searching with a Hint when query is empty,
 * and clear [IconButton] to clear query
 */
@Composable
fun SearchTextField(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onSearch: () -> Unit,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    Surface(
        modifier = modifier.then(Modifier.height(40.dp)),
        color = backgroundColor,
        shape = RoundedCornerShape(percent = 50)
    ) {
        Box(contentAlignment = Alignment.CenterStart) {
            if (query.text.isEmpty()) SearchHint(contentColor)

            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(contentColor),

                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .onFocusChanged { onSearchFocusChange(it.isFocused) }
                        .focusRequester(focusRequester)
                        .padding(
                            top = Dimensions.medium,
                            bottom = Dimensions.medium,
                            start = Dimensions.xLarge,
                            end = Dimensions.medium
                        ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                    cursorBrush = SolidColor(contentColor)
                )
            }
        }
    }
}

@Composable
private fun SearchHint(contentColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = Dimensions.xLarge, end = Dimensions.medium)
    ) {
        Text(
            color = contentColor.copy(alpha = 0.6f),
            text = stringResource(Res.string.search_hint)
        )
    }
}
