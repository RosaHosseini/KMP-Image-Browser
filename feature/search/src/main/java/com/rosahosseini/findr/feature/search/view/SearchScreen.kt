package com.rosahosseini.findr.feature.search.view

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.feature.search.model.SuggestionModel
import com.rosahosseini.findr.feature.search.view.components.LoadingScreen
import com.rosahosseini.findr.feature.search.view.components.SearchBarItem
import com.rosahosseini.findr.library.ui.R as UiR
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.component.CancelableChip
import com.rosahosseini.findr.ui.component.PhotosGridScreen
import com.rosahosseini.findr.ui.component.search.SearchDisplay
import com.rosahosseini.findr.ui.component.search.SearchState
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrColor
import kotlinx.collections.immutable.ImmutableList

@Suppress("LongParameterList")
@Composable
internal fun SearchScreen(
    photos: ImmutableList<Photo>,
    suggestions: ImmutableList<SuggestionModel>,
    isLoading: Boolean,
    errorMessage: String?,
    listState: LazyGridState,
    searchState: SearchState,
    onPhotoClick: (Photo) -> Unit,
    onToggleBookmark: (Photo) -> Unit,
    onBookmarksClick: () -> Unit,
    onCancelSuggestion: (SuggestionModel) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FindrColor.DarkBackground)
    ) {
        val alphaList = if (isLoading) 0.8f else 1f
        Column {
            TopHeader(
                searchState = searchState,
                onBookmarksClick = onBookmarksClick,
                suggestions = suggestions,
                onCancelSuggestion = onCancelSuggestion
            )
            PhotosGridScreen(
                photos = photos,
                onPhotoClick = onPhotoClick,
                onToggleBookmark = onToggleBookmark,
                modifier = Modifier.alpha(alphaList),
                listState = listState
            )
        }
        if (isLoading) LoadingScreen()
    }
    val context = LocalContext.current
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
private fun TopHeader(
    searchState: SearchState,
    suggestions: ImmutableList<SuggestionModel>,
    onBookmarksClick: () -> Unit,
    onCancelSuggestion: (SuggestionModel) -> Unit
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = Dimensions.defaultElevation
        )
    ) {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .background(FindrColor.DarkBackground)
                    .padding(Dimensions.defaultMarginHalf),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBarItem(
                    modifier = Modifier.weight(weight = 1f, fill = true),
                    state = searchState
                )
                Spacer(modifier = Modifier.width(Dimensions.defaultMarginDouble))
                BookmarksButton(onBookmarksClick)
                Spacer(modifier = Modifier.width(Dimensions.defaultMargin))
            }
            AnimatedVisibility(visible = searchState.searchDisplay == SearchDisplay.SUGGESTIONS) {
                SuggestionGridLayout(
                    suggestions = suggestions,
                    onCancelSuggestion = onCancelSuggestion,
                    onSuggestionClick = {
                        var query = searchState.query.text
                        if (query.isEmpty()) query = it.tag else query += " ${it.tag}"
                        query.trim()
                        // Set text and cursor position to end of text
                        searchState.query = TextFieldValue(query, TextRange(query.length))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SuggestionGridLayout(
    suggestions: ImmutableList<SuggestionModel>,
    onCancelSuggestion: (SuggestionModel) -> Unit,
    onSuggestionClick: (SuggestionModel) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .background(FindrColor.DarkBackground)
            .padding(
                start = Dimensions.defaultMarginDouble,
                end = Dimensions.defaultMarginDouble,
                bottom = Dimensions.defaultMargin
            )
    ) {
        suggestions.forEach { suggestionModel ->
            CancelableChip(
                modifier = Modifier.padding(Dimensions.defaultMarginHalf),
                tag = suggestionModel.tag,
                onClick = { onSuggestionClick(suggestionModel) },
                onCancel = { onCancelSuggestion(suggestionModel) }
            )
        }
    }
}

@Composable
private fun BookmarksButton(onBookmarksClick: () -> Unit) {
    Image(
        painter = painterResource(id = UiR.drawable.ic_heart),
        contentDescription = stringResource(id = UiR.string.bookmark),
        modifier = Modifier
            .fillMaxHeight()
            .widthIn(min = 36.dp)
            .clickable { onBookmarksClick() }
    )
}
