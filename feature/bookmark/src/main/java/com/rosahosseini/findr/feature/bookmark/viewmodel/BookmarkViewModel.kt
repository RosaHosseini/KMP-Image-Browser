package com.rosahosseini.findr.feature.bookmark.viewmodel

import androidx.lifecycle.viewModelScope
import com.rosahosseini.findr.domain.bookmark.BookmarkRepository
import com.rosahosseini.findr.feature.bookmark.state.BookmarkScreenState
import com.rosahosseini.findr.library.coroutines.base.BaseViewModel
import com.rosahosseini.findr.model.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
internal class BookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) : BaseViewModel() {

    val state: StateFlow<BookmarkScreenState> = bookmarkRepository
        .getAllBookmarkedPhotos()
        .map { BookmarkScreenState(it.toImmutableList()) }
        .stateIn(BookmarkScreenState())

    fun onToggleBookmark(photo: Photo) {
        viewModelScope.launch {
            bookmarkRepository.changeBookmarkState(photo.id, photo.isBookmarked.not())
        }
    }
}
