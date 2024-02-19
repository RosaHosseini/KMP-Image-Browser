package com.rosahosseini.findr.feature.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosahosseini.findr.domain.bookmark.BookmarkRepository
import com.rosahosseini.findr.domain.search.SearchRepository
import com.rosahosseini.findr.feature.search.model.SearchQueryModel
import com.rosahosseini.findr.feature.search.model.SuggestionModel
import com.rosahosseini.findr.model.Either
import com.rosahosseini.findr.model.ErrorModel
import com.rosahosseini.findr.model.Page
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.model.getError
import com.rosahosseini.findr.model.isFailure
import com.rosahosseini.findr.model.isLoading
import com.rosahosseini.findr.model.isSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class PhotoSearchViewModel @Inject internal constructor(
    private val searchRepository: SearchRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    private val latestSearchResponse = MutableStateFlow<Either<Page<Photo>>>(Either.Loading())

    private var searchJobs: MutableList<Job> = mutableListOf()
    private val searchQuery = MutableStateFlow(SearchQueryModel())

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchedPhotos: StateFlow<ImmutableList<Photo>> = searchQuery
        .flatMapLatest { queryPhotos(queryText = it.text, toPage = it.pageNumber) }
        .map { it.toImmutableList() }
        .stateIn(persistentListOf())

    val searchSuggestions: StateFlow<ImmutableList<SuggestionModel>> = getSearchSuggestion()
        .map { it.toImmutableList() }
        .stateIn(persistentListOf())

    val queryText: StateFlow<String> = searchQuery.map { it.text.orEmpty() }
        .stateIn("")

    val isLoading: Flow<Boolean> = latestSearchResponse.map { it.isLoading() }
    val error: Flow<ErrorModel?> = latestSearchResponse.map { it.getError() }

    private val _scrollToTop = MutableSharedFlow<Unit>(replay = 0)
    val scrollToTop: SharedFlow<Unit> = _scrollToTop

    init {
        onQueryTextChange("")
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
            query = queryText,
            fromPage = 0,
            toPage = toPage,
            PAGE_COUNT
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

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> {
        return stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue
        )
    }

    companion object {
        private const val PAGE_COUNT = 25
        private const val SUGGESTION_LIMIT = 6
        private const val STOP_TIMEOUT_MILLIS = 5L
    }
}
