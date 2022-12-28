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
import com.rosahosseini.bleacher.search.model.SearchQueryModel
import com.rosahosseini.bleacher.search.model.SuggestionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class PhotoSearchViewModel @Inject internal constructor(
    private val searchRepository: SearchRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val navigator: Navigator
) : BaseViewModel(navigator) {

    private val latestSearchResponse = MutableStateFlow<Either<Page<Photo>>>(Either.Loading())

    private var searchJobs: MutableList<Job> = mutableListOf()
    private val searchQuery = MutableStateFlow(SearchQueryModel())

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchedPhotos: StateFlow<List<Photo>> = searchQuery
        .flatMapLatest { queryPhotos(queryText = it.text, toPage = it.pageNumber) }
        .stateIn(emptyList())

    val searchSuggestions: StateFlow<List<SuggestionModel>> = getSearchSuggestion()
        .stateIn(emptyList())

    val isLoading: Flow<Boolean> = latestSearchResponse.map { it.isLoading() }
    val error: Flow<ErrorModel?> = latestSearchResponse.map { it.getError() }

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

    fun onCancelSearchSuggestion(suggestionModel: SuggestionModel) {
        viewModelScope.launch {
            searchRepository.removeSuggestion(suggestionModel.tag)
        }
    }

    fun onQueryTextChange(newQuery: String) {
        // if query is repeated and latest state is success don't search again
        if ((newQuery == searchQuery.value.text) and latestSearchResponse.value.isSuccess()) {
            return
        }
        searchJobs.forEach { it.cancel() }
        searchJobs.clear()
        searchPhotoPage(SearchQueryModel(text = newQuery))
        saveToSearchHistory(newQuery)
    }

    fun onLoadMore() {
        with(latestSearchResponse.value) {
            val hasNext = data?.hasNext ?: false
            val lastQuery = searchQuery.value
            when {
                isFailure() -> searchPhotoPage(lastQuery)
                isSuccess() and hasNext -> searchPhotoPage(lastQuery.nextPageQuery)
            }
        }
    }

    private fun searchPhotoPage(queryModel: SearchQueryModel) {
        viewModelScope.launch {
            searchQuery.value = queryModel
            if (queryModel.pageNumber == 0) _scrollToTop.emit(Unit)
            val result = if (queryModel.text?.isNotBlank() != true) {
                searchRepository.getRecentPhotos(queryModel.pageNumber, limit = PAGE_COUNT)
            } else {
                searchRepository.searchPhotos(
                    queryModel.text,
                    queryModel.pageNumber,
                    limit = PAGE_COUNT
                )
            }
            result.collect {
                latestSearchResponse.value = it
            }
        }.also { searchJobs.add(it) }
    }

    private fun queryPhotos(queryText: String?, toPage: Int): Flow<List<Photo>> {
        return searchRepository.searchLocalPhotos(
            query = queryText, fromPage = 0, toPage = toPage, PAGE_COUNT
        )
    }

    private fun saveToSearchHistory(queryText: String) {
        viewModelScope.launch {
            if (queryText.isNotBlank()) {
                searchRepository.saveSearchQuery(queryText)
            }
        }
    }

    private fun getSearchSuggestion(): Flow<List<SuggestionModel>> {
        return searchRepository.getSearchSuggestion("", SUGGESTION_LIMIT)
            .map { it.map { SuggestionModel(it) } }
    }

    companion object {
        private const val PAGE_COUNT = 25
        private const val SUGGESTION_LIMIT = 6
    }
}