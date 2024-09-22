package com.rosahosseini.findr.feature.bookmark.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.domain.model.Photo
import com.rosahosseini.findr.feature.bookmark.view.components.BookmarkTopAppBar
import com.rosahosseini.findr.feature.bookmark.view.preview.BookmarkScreenPreviewProvider
import com.rosahosseini.findr.feature.bookmark.viewmodel.BookmarkContract
import com.rosahosseini.findr.library.ui.Res
import com.rosahosseini.findr.library.ui.add_bookmarks
import com.rosahosseini.findr.library.ui.empty_bookmarks
import com.rosahosseini.findr.ui.component.PhotoCard
import com.rosahosseini.findr.ui.component.state.ErrorComponent
import com.rosahosseini.findr.ui.component.state.LoadingComponent
import com.rosahosseini.findr.ui.extensions.localMessage
import com.rosahosseini.findr.ui.state.UiState
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrTheme
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BookmarkScreen(
    state: BookmarkContract.State,
    onPhotoClick: (Photo) -> Unit,
    onBookmarkClick: (Photo) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = { BookmarkTopAppBar(onBackPressed, scrollBehavior) },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            state.photos.data?.let { photos ->
                PhotosGrid(
                    photos = photos,
                    isBookmarked = state::isBookmarked,
                    onItemClick = onPhotoClick,
                    onBookmarkClick = onBookmarkClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                )
            }
            when (val photosState = state.photos) {
                is UiState.Loading ->
                    LoadingComponent(modifier = Modifier.fillMaxSize())

                is UiState.Failure ->
                    ErrorComponent(
                        message = photosState.throwable.localMessage,
                        onActionClick = null,
                        modifier = Modifier.padding(Dimensions.xxLarge)
                    )

                is UiState.Success -> if (photosState.data.isEmpty()) {
                    ErrorComponent(
                        message = stringResource(Res.string.empty_bookmarks),
                        onActionClick = onBackPressed,
                        actionLabel = stringResource(Res.string.add_bookmarks),
                        modifier = Modifier.padding(Dimensions.xxLarge)
                    )
                }

                else -> {}
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PhotosGrid(
    photos: ImmutableList<Photo>,
    isBookmarked: (String) -> Boolean,
    onBookmarkClick: (Photo) -> Unit,
    onItemClick: (Photo) -> Unit,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState()
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        state = gridState,
        modifier = modifier
    ) {
        items(photos, contentType = { "image-card" }) { item ->
            PhotoCard(
                photo = item,
                isBookmarked = isBookmarked(item.id),
                onBookmarkClick = { onBookmarkClick(item) },
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(Dimensions.xSmall)
                    .clickable { onItemClick(item) }
            )
        }
    }
}

@Preview
@Composable
private fun BookmarkScreenPreview(
    @PreviewParameter(BookmarkScreenPreviewProvider::class) state: BookmarkContract.State
) {
    FindrTheme {
        BookmarkScreen(
            state = state,
            onBackPressed = {},
            onBookmarkClick = {},
            onPhotoClick = {}
        )
    }
}
