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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.feature.bookmark.view.components.Toolbar
import com.rosahosseini.findr.feature.bookmark.viewmodel.BookmarkContract
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.component.LoadingComponent
import com.rosahosseini.findr.ui.component.PhotoCard
import com.rosahosseini.findr.ui.component.PhotosGrid
import com.rosahosseini.findr.ui.state.UiState
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrColor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BookmarkScreen(
    state: BookmarkContract.State,
    onPhotoClick: (Photo) -> Unit,
    onBookmarkClick: (Photo) -> Unit,
    onBackPressed: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = { Toolbar(onBackPressed, scrollBehavior) },
        containerColor = FindrColor.DarkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            state.photos.data?.let { photos ->
                PhotosGrid(
                    photos = photos,
                    bookmarks = state.bookmarks,
                    onPhotoClick = onPhotoClick,
                    onItemBookmarkClick = onBookmarkClick
                )
            }
            when (val photosState = state.photos) {
                is UiState.Failure -> {
                    // todo
                }

                is UiState.Loading -> {
                    LoadingComponent(modifier = Modifier.fillMaxSize())
                }

                is UiState.Success -> if (photosState.data.isEmpty()) {
                    // todo
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
    bookmarks: ImmutableMap<String, Boolean>,
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
                isBookmarked = bookmarks[item.id] ?: false,
                onBookmarkClick = { onBookmarkClick(item) },
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(Dimensions.defaultMarginQuarter)
                    .clickable { onItemClick(item) }
            )
        }
    }
}
