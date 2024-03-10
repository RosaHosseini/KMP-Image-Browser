package com.rosahosseini.findr.feature.search.view.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.library.ui.R as UiR
import com.rosahosseini.findr.ui.component.CancelableChip
import com.rosahosseini.findr.ui.component.search.SearchBarComponent
import com.rosahosseini.findr.ui.extensions.doOncePerInstance
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun SearchTopAppBar(
    term: String,
    suggestions: ImmutableList<String>,
    onBookmarksClick: () -> Unit,
    onTermChange: (String) -> Unit,
    onRemoveSuggestion: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var focused by remember { mutableStateOf(false) }
    var termTextField: TextFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = term, selection = TextRange(term.length)))
    }
    termTextField.text.trim().doOncePerInstance(block = onTermChange)

    val focusManager = LocalFocusManager.current

    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = Dimensions.elevation),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(horizontal = Dimensions.medium)) {
            Row(
                Modifier
                    .height(IntrinsicSize.Min)
                    .padding(vertical = Dimensions.medium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimensions.medium)
            ) {
                SearchBarComponent(
                    query = termTextField,
                    onQueryChange = { termTextField = it },
                    onBackClick = { termTextField = TextFieldValue() },
                    onSearchFocusChange = { focused = it },
                    focused = focused,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(id = UiR.drawable.ic_heart),
                    contentDescription = stringResource(id = UiR.string.bookmark),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = Dimensions.medium)
                        .clip(CircleShape)
                        .clickable { onBookmarksClick() }
                        .widthIn(min = 36.dp)
                )
            }
            AnimatedVisibility(visible = focused && suggestions.isNotEmpty()) {
                Suggestions(
                    items = suggestions,
                    onRemoveItem = onRemoveSuggestion,
                    onSelectItem = {
                        focusManager.clearFocus()
                        termTextField = TextFieldValue(it, selection = TextRange(it.length))
                    },
                    modifier = Modifier.padding(bottom = Dimensions.large)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Suggestions(
    items: ImmutableList<String>,
    onSelectItem: (String) -> Unit,
    onRemoveItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = items,
        label = "suggestions",
        modifier = Modifier.fillMaxWidth()
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Dimensions.medium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.medium),
            modifier = modifier
        ) {
            it.forEach { term ->
                CancelableChip(
                    tag = term,
                    onClick = { onSelectItem(term) },
                    onCancel = { onRemoveItem(term) }
                )
            }
        }
    }
}

@Composable
@Preview
private fun SearchTopAppBarPreview() {
    FindrTheme {
        SearchTopAppBar(
            term = "",
            suggestions = persistentListOf("term1", "term2"),
            onBookmarksClick = {},
            onTermChange = {},
            onRemoveSuggestion = {}
        )
    }
}
