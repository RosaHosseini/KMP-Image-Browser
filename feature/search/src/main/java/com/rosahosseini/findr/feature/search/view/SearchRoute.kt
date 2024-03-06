package com.rosahosseini.findr.feature.search.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rosahosseini.findr.feature.bookmark.viewmodel.BookmarkContract.Intent as BookmarkIntent
import com.rosahosseini.findr.feature.bookmark.viewmodel.BookmarkViewModel
import com.rosahosseini.findr.feature.search.viewmodel.SearchContract.Intent as SearchIntent
import com.rosahosseini.findr.feature.search.viewmodel.SearchViewModel

@Composable
internal fun SearchRoute(
    navigateToPhotoDetail: (
        url: String,
        title: String?,
        description: String?
    ) -> Unit,
    navigateToBookmarks: () -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel(),
    bookmarkViewModel: BookmarkViewModel = hiltViewModel()
) {
    val searchState by searchViewModel.state.collectAsStateWithLifecycle()
    val bookmarkState by bookmarkViewModel.state.collectAsStateWithLifecycle()

    SearchScreen(
        searchState = searchState,
        isBookmarked = bookmarkState::isBookmarked,
        onPhotoClick = { navigateToPhotoDetail(it.url, it.title, it.description) },
        onItemBookmarkClick = { photo ->
            val enabled = bookmarkState.isBookmarked(photo.id)
            bookmarkViewModel.onIntent(BookmarkIntent.OnUpdateBookmark(photo, !enabled))
        },
        onBookmarksClick = navigateToBookmarks,
        onRemoveSuggestion = { searchViewModel.onIntent(SearchIntent.OnRemoveSuggestion(it)) },
        onLoadMore = { searchViewModel.onIntent(SearchIntent.OnLoadMore) },
        onTermChange = { searchViewModel.onIntent(SearchIntent.OnTermChange(it)) }
    )
}
