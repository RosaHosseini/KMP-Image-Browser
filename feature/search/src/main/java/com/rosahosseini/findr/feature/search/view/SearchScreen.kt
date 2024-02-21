package com.rosahosseini.findr.feature.search.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.feature.search.view.components.SearchTopAppBar
import com.rosahosseini.findr.feature.search.viewmodel.SearchContract
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.component.LoadingComponent
import com.rosahosseini.findr.ui.component.PhotoCard
import com.rosahosseini.findr.ui.extensions.OnBottomReached
import com.rosahosseini.findr.ui.state.PagingState
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrColor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

@Suppress("LongParameterList")
@Composable
internal fun SearchScreen(
    searchState: SearchContract.State,
    bookmarks: ImmutableMap<String, Boolean>,
    onPhotoClick: (Photo) -> Unit,
    onItemBookmarkClick: (Photo) -> Unit,
    onBookmarksClick: () -> Unit,
    onRemoveSuggestion: (String) -> Unit,
    onTermChange: (String) -> Unit,
    onLoadMore: () -> Unit
) {
    val gridState = rememberLazyGridState()
    Scaffold(
        topBar = {
            SearchTopAppBar(
                term = searchState.term,
                onBookmarksClick = onBookmarksClick,
                suggestions = searchState.suggestions,
                onTermChange = onTermChange,
                onRemoveSuggestion = onRemoveSuggestion
            )
        },
        containerColor = FindrColor.DarkBackground
    ) { paddingValues ->
        val photosState = searchState.photos
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            state = gridState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (photosState.status == PagingState.Status.Refreshing) loadingItem()
            photoItems(
                photos = photosState.data,
                bookmarks = bookmarks,
                onBookmarkClick = onItemBookmarkClick,
                onItemClick = onPhotoClick
            )
            when (photosState.status) {
                PagingState.Status.Failure -> photosState.throwable?.let(::errorItem)
                PagingState.Status.Loading -> loadingItem()
                else -> {}
            }
        }
    }

    gridState.OnBottomReached(buffer = 2) {
        onLoadMore()
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyGridScope.photoItems(
    photos: ImmutableList<Photo>,
    bookmarks: ImmutableMap<String, Boolean>,
    onBookmarkClick: (Photo) -> Unit,
    onItemClick: (Photo) -> Unit
) {
    items(photos, contentType = { "image-card" }) { item ->
        PhotoCard(
            photo = item,
            isBookmarked = bookmarks[item.id] ?: false,
            onBookmarkClick = { onBookmarkClick(item) },
            modifier = Modifier
                .animateItemPlacement()
                .padding(Dimensions.defaultMarginQuarter)
                .clickable { onItemClick(item) }
        )
    }
}

private fun LazyGridScope.loadingItem() {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = "loading"
    ) {
        LoadingComponent(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.defaultMarginDouble)
        )
    }
}

private fun LazyGridScope.errorItem(throwable: Throwable) {
    // todo
}
