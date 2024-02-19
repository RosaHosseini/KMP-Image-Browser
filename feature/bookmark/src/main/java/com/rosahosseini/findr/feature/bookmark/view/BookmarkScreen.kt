package com.rosahosseini.findr.feature.bookmark.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.rosahosseini.findr.feature.bookmark.view.components.Toolbar
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.component.PhotosGridScreen
import com.rosahosseini.findr.ui.theme.FindrColor
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BookmarkScreen(
    photos: ImmutableList<Photo>,
    onPhotoClick: (Photo) -> Unit,
    onToggleBookmark: (Photo) -> Unit,
    onBackPressed: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = { Toolbar(onBackPressed, scrollBehavior) },
        containerColor = FindrColor.DarkBackground
    ) { paddingValues ->
        PhotosGridScreen(
            photos = photos,
            onPhotoClick = onPhotoClick,
            onToggleBookmark = onToggleBookmark,
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(paddingValues)
        )
    }
}
