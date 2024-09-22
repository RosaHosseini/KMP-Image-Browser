package com.rosahosseini.findr.feature.bookmark.view.preview

import com.rosahosseini.findr.domain.model.Photo
import com.rosahosseini.findr.feature.bookmark.viewmodel.BookmarkContract
import com.rosahosseini.findr.ui.state.UiState
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

internal class BookmarkScreenPreviewProvider : PreviewParameterProvider<BookmarkContract.State> {
    private val photo = Photo("1", "title", "desc", "", null)
    private val photos = persistentListOf(
        photo,
        photo.copy(id = "2"),
        photo.copy(id = "3"),
        photo.copy(id = "4")
    )
    private val bookmarks: ImmutableMap<String, Boolean> = persistentMapOf("1" to true)

    override val values: Sequence<BookmarkContract.State> = sequenceOf(
        BookmarkContract.State(
            bookmarks = bookmarks,
            photos = UiState.Success(photos)
        ),
        BookmarkContract.State(
            bookmarks = bookmarks,
            photos = UiState.Success(data = persistentListOf())
        ),
        BookmarkContract.State(
            bookmarks = bookmarks,
            photos = UiState.Loading(photos)
        ),
        BookmarkContract.State(
            bookmarks = bookmarks,
            photos = UiState.Failure(
                data = photos,
                throwable = Throwable("some error happened")
            )
        )
    )
}
