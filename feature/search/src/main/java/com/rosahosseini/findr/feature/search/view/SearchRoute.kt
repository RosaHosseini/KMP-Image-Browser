package com.rosahosseini.findr.feature.search.view

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rosahosseini.findr.feature.search.viewmodel.PhotoSearchViewModel
import com.rosahosseini.findr.ui.component.search.rememberSearchState
import com.rosahosseini.findr.ui.extensions.OnBottomReached
import kotlinx.coroutines.flow.collectLatest

private const val DEBOUNCE_TIME_MILLIS = 1000L

@Composable
internal fun SearchRoute(
    navigateToPhotoDetail: (
        url: String,
        title: String?,
        description: String?
    ) -> Unit,
    navigateToBookmarks: () -> Unit,
    searchViewModel: PhotoSearchViewModel = hiltViewModel()
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
        suggestions = suggestions,
        onCancelSuggestion = searchViewModel::onCancelSearchSuggestion
    )
}
