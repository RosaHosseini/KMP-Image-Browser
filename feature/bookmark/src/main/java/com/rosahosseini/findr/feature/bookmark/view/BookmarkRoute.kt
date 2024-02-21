package com.rosahosseini.findr.feature.bookmark.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rosahosseini.findr.feature.bookmark.viewmodel.BookmarkContract.Intent
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
        state = state,
        onPhotoClick = { navigateToPhotoDetail(it.url, it.title, it.description) },
        onBookmarkClick = {
            val enabled = !(state.bookmarks[it.id] ?: false)
            bookmarkViewModel.onIntent(Intent.OnUpdateBookmark(it, enabled))
        },
        onBackPressed = onBackPressed
    )
}
