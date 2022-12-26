package com.rosahosseini.bleacher.search.viewmodel

import androidx.lifecycle.viewModelScope
import com.rosahosseini.bleacher.core.base.BaseViewModel
import com.rosahosseini.bleacher.model.Either
import com.rosahosseini.bleacher.model.ErrorModel
import com.rosahosseini.bleacher.model.Page
import com.rosahosseini.bleacher.model.Photo
import com.rosahosseini.bleacher.model.getError
import com.rosahosseini.bleacher.model.isFailure
import com.rosahosseini.bleacher.model.isLoading
import com.rosahosseini.bleacher.model.isSuccess
import com.rosahosseini.bleacher.navigation.Navigator
import com.rosahosseini.bleacher.navigation.destinations.BookmarkDestination
import com.rosahosseini.bleacher.navigation.destinations.PhotoDetailDestination
import com.rosahosseini.bleacher.repository.BookmarkRepository
import com.rosahosseini.bleacher.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class PhotoSearchViewModel @Inject internal constructor(
    private val searchRepository: SearchRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val navigator: Navigator
) : BaseViewModel(navigator) {

    private val queryFlow = MutableStateFlow<String?>(null)
    private val latestSearchPageFlow = MutableStateFlow<Either<Page<Photo>>>(Either.Loading())

    @OptIn(FlowPreview::class)
    val searchedPhotosFlow: StateFlow<List<Photo>> = queryFlow.flatMapMerge {
        searchRepository.searchLocalPhotos(it)
    }.stateIn(emptyList())

    val loadingFlow: StateFlow<Boolean> = latestSearchPageFlow
        .map { it.isLoading() }
        .stateIn(initialValue = false)

    val errorFlow: StateFlow<ErrorModel?> = latestSearchPageFlow
        .map { it.getError() }
        .stateIn(initialValue = null)

    private val _scrollToTop = MutableSharedFlow<Unit>(replay = 0)
    val scrollToTop: SharedFlow<Unit> = _scrollToTop

    private var searchJobs: MutableList<Job> = mutableListOf()

    init {
        onQueryTextChange("")
    }

    fun onPhotoClick(photo: Photo) {
        viewModelScope.launch {
            navigator.navigateTo(
                navTarget = PhotoDetailDestination,
                args = listOf(Pair(PhotoDetailDestination.arg, photo))
            )
        }
    }

    fun onQueryTextChange(newQuery: String) {
        // if query is repeated and latest state is success don't search again
        if ((newQuery == queryFlow.value) and latestSearchPageFlow.value.isSuccess()) return
        clearSearchJobs()
        viewModelScope.launch {
            // delay for debouncing redundant queries
            delay(DEBOUNCE_MILLI_SECONDS)
            queryFlow.value = newQuery
            searchPhoto(newQuery)
        }.also { searchJobs.add(it) }
    }

    fun onLoadMore() {
        val latestResult = latestSearchPageFlow.value
        val currentPage = latestResult.data ?: return
        when {
            latestResult.isFailure() -> {
                loadPhotos(currentPage.pageNumber)
            }
            latestResult.isSuccess() and currentPage.hasNext -> {
                loadPhotos(currentPage.pageNumber + 1)
            }
        }
    }

    fun onToggleBookmark(photo: Photo) {
        viewModelScope.launch {
            bookmarkRepository.changeBookmarkState(photo.id, photo.isBookmarked.not())
        }
    }

    fun onBookmarksClick() {
        viewModelScope.launch {
            navigator.navigateTo(BookmarkDestination)
        }
    }

    private fun loadPhotos(pageNumber: Int) {
        viewModelScope.launch {
            searchPhoto(queryFlow.value, pageNumber)
        }.also { searchJobs.add(it) }
    }

    private suspend fun searchPhoto(queryText: String?, pageNumber: Int = 0) {
        if (pageNumber == 0) _scrollToTop.emit(Unit)
        val result = if (queryText?.isNotBlank() != true) {
            searchRepository.getRecentPhotos(pageNumber, limit = PHOTOS_COUNT)
        } else {
            searchRepository.searchPhotos(queryText, pageNumber, limit = PHOTOS_COUNT)
        }
        result.collect {
            latestSearchPageFlow.value = it
        }
    }

    private fun clearSearchJobs() {
        searchJobs.forEach { it.cancel() }
        searchJobs.clear()
    }

    companion object {
        private const val DEBOUNCE_MILLI_SECONDS: Long = 500
        private const val PHOTOS_COUNT = 25
    }
}