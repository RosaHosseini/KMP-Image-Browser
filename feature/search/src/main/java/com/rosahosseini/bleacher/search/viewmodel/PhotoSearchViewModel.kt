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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class PhotoSearchViewModel @Inject internal constructor(
    private val searchRepository: SearchRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val navigator: Navigator
) : BaseViewModel(navigator) {

    private var searchJobs: MutableList<Job> = mutableListOf()
    private val queryFlow = MutableStateFlow<String?>(null)
    private val latestSearchPageFlow = MutableStateFlow<Either<Page<Photo>>>(Either.Loading())

    private var currentPageNumber: StateFlow<Int> = latestSearchPageFlow
        .map { it.data?.pageNumber }
        .filterNotNull()
        .stateIn(0)

    @OptIn(FlowPreview::class)
    val searchedPhotosFlow: StateFlow<List<Photo>> = queryFlow
        .combine(currentPageNumber) { query, page -> Pair(query, page) }
        .flatMapMerge {
            searchRepository.searchLocalPhotos(
                query = it.first, fromPage = 0, toPage = it.second, PAGE_COUNT
            )
        }.stateIn(emptyList())

    val loadingFlow: StateFlow<Boolean> = latestSearchPageFlow
        .map { it.isLoading() }
        .stateIn(initialValue = false)

    val errorFlow: StateFlow<ErrorModel?> = latestSearchPageFlow
        .map { it.getError() }
        .stateIn(initialValue = null)

    private val _scrollToTop = MutableSharedFlow<Unit>(replay = 0)
    val scrollToTop: SharedFlow<Unit> = _scrollToTop

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
        searchJobs.forEach { it.cancel() }
        searchJobs.clear()
        viewModelScope.launch {
            queryFlow.value = newQuery
            searchPhoto(newQuery)
        }.also { searchJobs.add(it) }
    }

    fun onBookmarksClick() {
        viewModelScope.launch {
            navigator.navigateTo(BookmarkDestination)
        }
    }

    fun onToggleBookmark(photo: Photo) {
        viewModelScope.launch {
            bookmarkRepository.changeBookmarkState(photo.id, photo.isBookmarked.not())
        }
    }

    fun onLoadMore() {
        getSearchNextPageNumber()?.let { nextPage ->
            viewModelScope.launch {
                searchPhoto(queryFlow.value, nextPage)
            }.also { searchJobs.add(it) }
        }
    }

    /** next page number of search photos,
     * returns null if we are not allowed to load more **/
    private fun getSearchNextPageNumber(): Int? {
        val latestResult = latestSearchPageFlow.value
        val hasNext = latestResult.data?.hasNext ?: false
        return when {
            latestResult.isFailure() -> {
                currentPageNumber.value
            }
            latestResult.isSuccess() and hasNext -> {
                currentPageNumber.value + 1
            }
            else -> null
        }
    }

    private suspend fun searchPhoto(queryText: String?, pageNumber: Int = 0) {
        if (pageNumber == 0) _scrollToTop.emit(Unit)
        val result = if (queryText?.isNotBlank() != true) {
            searchRepository.getRecentPhotos(pageNumber, limit = PAGE_COUNT)
        } else {
            searchRepository.searchPhotos(queryText, pageNumber, limit = PAGE_COUNT)
        }
        result.collect {
            latestSearchPageFlow.value = it
        }
    }

    companion object {
        private const val PAGE_COUNT = 25
    }
}