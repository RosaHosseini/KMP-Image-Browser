package com.rosahosseini.findr.search.view

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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.search.model.SuggestionModel
import com.rosahosseini.findr.search.view.components.LoadingScreen
import com.rosahosseini.findr.search.view.components.SearchBarItem
import com.rosahosseini.findr.search.viewmodel.PhotoSearchViewModel
import com.rosahosseini.findr.ui.component.CancelableChip
import com.rosahosseini.findr.ui.component.PhotosGridScreen
import com.rosahosseini.findr.ui.component.search.SearchDisplay
import com.rosahosseini.findr.ui.component.search.SearchState
import com.rosahosseini.findr.ui.component.search.rememberSearchState
import com.rosahosseini.findr.ui.extensions.OnBottomReached
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrColor
import kotlinx.coroutines.flow.collectLatest
import com.rosahosseini.findr.library.ui.R as UiR

private const val DEBOUNCE_TIME_MILLIS = 1000L

@Composable
fun SearchRoute(
    navigateToPhotoDetail: (
        url: String,
        title: String?,
        description: String?,
    ) -> Unit,
    navigateToBookmarks: () -> Unit,
    searchViewModel: PhotoSearchViewModel = hiltViewModel(),
) {
    val searchedPhotos by searchViewModel.searchedPhotos.collectAsStateWithLifecycle()
    val suggestions by searchViewModel.searchSuggestions.collectAsStateWithLifecycle()
    val isLoading by searchViewModel.isLoading.collectAsStateWithLifecycle(initialValue = false)
    val error by searchViewModel.error.collectAsStateWithLifecycle(initialValue = null)
    val queryText by searchViewModel.queryText.collectAsStateWithLifecycle()
    val listState = rememberLazyGridState()
    val searchState = rememberSearchState(
        initialQuery = queryText,
        debounceMillis = DEBOUNCE_TIME_MILLIS,
        onQueryChange = searchViewModel::onQueryTextChange
    )
    listState.OnBottomReached(buffer = 1) {
        searchViewModel.onLoadMore()
    }
    LaunchedEffect(key1 = Unit) {
        searchViewModel.scrollToTop.collectLatest {
            listState.scrollToItem(0)
        }
    }

    SearchScreen(
        photos = searchedPhotos,
        isLoading = isLoading,
        errorMessage = error?.localMessage?.let { stringResource(it) } ?: error?.message,
        listState = listState,
        searchState = searchState,
        onPhotoClick = { navigateToPhotoDetail(it.urlOriginal, it.title, it.description) },
        onToggleBookmark = searchViewModel::onToggleBookmark,
        onBookmarksClick = navigateToBookmarks,
        suggestions = { suggestions },
        onCancelSuggestion = searchViewModel::onCancelSearchSuggestion
    )
}

@Composable
private fun SearchScreen(
    photos: List<Photo>,
    isLoading: Boolean,
    errorMessage: String?,
    listState: LazyGridState,
    searchState: SearchState,
    onPhotoClick: (Photo) -> Unit,
    onToggleBookmark: (Photo) -> Unit,
    onBookmarksClick: () -> Unit,
    suggestions: () -> List<SuggestionModel>,
    onCancelSuggestion: (SuggestionModel) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FindrColor.DarkBackground)
    ) {
        val alphaList = if (isLoading) 0.8f else 1f
        Column {
            TopHeader(searchState, onBookmarksClick, suggestions, onCancelSuggestion)
            PhotosGridScreen(
                photos,
                onPhotoClick,
                onToggleBookmark,
                Modifier.alpha(alphaList),
                listState
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
    onBookmarksClick: () -> Unit,
    suggestions: () -> List<SuggestionModel>,
    onCancelSuggestion: (SuggestionModel) -> Unit,
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = Dimensions.defaultElevation)
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
                    Modifier.weight(weight = 1f, fill = true),
                    searchState,
                )
                Spacer(modifier = Modifier.width(Dimensions.defaultMarginDouble))
                BookmarksButton(onBookmarksClick)
                Spacer(modifier = Modifier.width(Dimensions.defaultMargin))
            }
            AnimatedVisibility(visible = searchState.searchDisplay == SearchDisplay.SUGGESTIONS) {
                SuggestionGridLayout(
                    suggestions = suggestions(),
                    onCancelSuggestion = onCancelSuggestion
                ) {
                    var query = searchState.query.text
                    if (query.isEmpty()) query = it.tag else query += " ${it.tag}"
                    query.trim()
                    // Set text and cursor position to end of text
                    searchState.query = TextFieldValue(query, TextRange(query.length))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SuggestionGridLayout(
    modifier: Modifier = Modifier,
    suggestions: List<SuggestionModel>,
    onCancelSuggestion: (SuggestionModel) -> Unit,
    onSuggestionClick: (SuggestionModel) -> Unit,
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
                onCancel = { onCancelSuggestion(suggestionModel) },
            )
        }
    }
}

@Composable
fun BookmarksButton(onBookmarksClick: () -> Unit) {
    Image(
        painter = painterResource(id = UiR.drawable.ic_heart),
        contentDescription = stringResource(id = UiR.string.bookmark),
        modifier = Modifier
            .fillMaxHeight()
            .widthIn(min = 36.dp)
            .clickable { onBookmarksClick() }
    )
}