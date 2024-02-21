package com.rosahosseini.findr.feature.search.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.library.ui.R as UiR
import com.rosahosseini.findr.ui.component.CancelableChip
import com.rosahosseini.findr.ui.component.search.SearchDisplay
import com.rosahosseini.findr.ui.component.search.rememberSearchState
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrColor
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun SearchTopAppBar(
    term: String,
    suggestions: ImmutableList<String>,
    onBookmarksClick: () -> Unit,
    onTermChange: (String) -> Unit,
    onRemoveSuggestion: (String) -> Unit
) {
    val searchState = rememberSearchState(initialQuery = term, onQueryChange = onTermChange)

    Card(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = Dimensions.defaultElevation
        ),
        colors = CardDefaults.cardColors(containerColor = FindrColor.DarkBackground)
    ) {
        Column(modifier = Modifier.padding(horizontal = Dimensions.defaultMargin)) {
            Row(
                Modifier
                    .height(IntrinsicSize.Min)
                    .padding(vertical = Dimensions.defaultMargin),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimensions.defaultMargin)
            ) {
                SearchBarItem(
                    modifier = Modifier.weight(weight = 1f, fill = true),
                    state = searchState
                )
                Image(
                    painter = painterResource(id = UiR.drawable.ic_heart),
                    contentDescription = stringResource(id = UiR.string.bookmark),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = Dimensions.defaultMargin)
                        .clip(CircleShape)
                        .clickable { onBookmarksClick() }
                        .widthIn(min = 36.dp)
                )
            }
            AnimatedVisibility(visible = searchState.searchDisplay == SearchDisplay.SUGGESTIONS) {
                Suggestions(
                    items = suggestions,
                    onRemoveItem = onRemoveSuggestion,
                    onSelectItem = {
                        searchState.termTextField = TextFieldValue(term, TextRange(term.length))
                    },
                    modifier = Modifier.padding(
                        top = Dimensions.defaultMargin,
                        bottom = Dimensions.defaultMarginDouble
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
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(Dimensions.defaultMargin),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.defaultMargin),
        modifier = modifier
    ) {
        items.forEach { term ->
            CancelableChip(
                tag = term,
                onClick = { onSelectItem(term) },
                onCancel = { onRemoveItem(term) }
            )
        }
    }
}
