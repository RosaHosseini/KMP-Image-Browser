package com.rosahosseini.findr.feature.bookmark.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rosahosseini.findr.feature.bookmark.viewmodel.BookmarkViewModel

@Composable
internal fun BookmarkRoute(
    navigateToPhotoDetail: (
        url: String,
        title: String?,
        description: String?
    ) -> Unit,
    onBackPressed: () -> Unit,
    bookmarkViewModel: BookmarkViewModel = hiltViewModel()
) {
    val state by bookmarkViewModel.state.collectAsStateWithLifecycle()
    BookmarkScreen(
        photos = state.photos,
        onPhotoClick = { navigateToPhotoDetail(it.url, it.title, it.description) },
        onToggleBookmark = bookmarkViewModel::onToggleBookmark,
        onBackPressed = onBackPressed
    )
}
