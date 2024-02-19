package com.rosahosseini.findr.feature.bookmark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosahosseini.findr.domain.bookmark.BookmarkRepository
import com.rosahosseini.findr.model.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class BookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    val bookmarkedPhotos: StateFlow<ImmutableList<Photo>> =
        bookmarkRepository.getAllBookmarkedPhotos()
            .map { it.toImmutableList() }
            .stateIn(
                viewModelScope,
                started = SharingStarted.WhileSubscribed(5L),
                persistentListOf()
            )

    fun onToggleBookmark(photo: Photo) {
        viewModelScope.launch {
            bookmarkRepository.changeBookmarkState(photo.id, photo.isBookmarked.not())
        }
    }
}
