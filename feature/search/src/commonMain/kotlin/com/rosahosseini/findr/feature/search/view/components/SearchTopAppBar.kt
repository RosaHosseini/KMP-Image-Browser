package com.rosahosseini.findr.feature.search.view.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.library.ui.Res as UiR
import com.rosahosseini.findr.library.ui.bookmark
import com.rosahosseini.findr.library.ui.ic_heart
import com.rosahosseini.findr.ui.component.CancelableChip
import com.rosahosseini.findr.ui.component.search.SearchBarComponent
import com.rosahosseini.findr.ui.component.search.SearchDisplay
import com.rosahosseini.findr.ui.component.search.rememberSearchState
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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
    val searchState = rememberSearchState(initialQuery = term, onQueryChange = onTermChange)
    val showSuggestions = searchState.searchDisplay == SearchDisplay.SUGGESTIONS &&
        suggestions.isNotEmpty()
    val focusManager = LocalFocusManager.current

    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = Dimensions.elevation),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Row(
                Modifier
                    .height(IntrinsicSize.Min)
                    .padding(vertical = Dimensions.medium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimensions.medium)
            ) {
                SearchBarComponent(
                    state = searchState,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(UiR.drawable.ic_heart),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = stringResource(UiR.string.bookmark),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = Dimensions.medium)
                        .clip(CircleShape)
                        .clickable { onBookmarksClick() }
                        .widthIn(min = 36.dp)
                )
            }
            AnimatedVisibility(visible = showSuggestions) {
                Suggestions(
                    items = suggestions,
                    onRemoveItem = onRemoveSuggestion,
                    onSelectItem = {
                        focusManager.clearFocus()
                        searchState.termTextField = TextFieldValue(it)
                    },
                    modifier = Modifier.padding(
                        bottom = Dimensions.large,
                        start = Dimensions.medium,
                        end = Dimensions.medium
                    )
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
            modifier = modifier,
            maxLines = 2
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
