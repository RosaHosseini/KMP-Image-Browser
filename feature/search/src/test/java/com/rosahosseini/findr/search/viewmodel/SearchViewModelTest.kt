package com.rosahosseini.findr.search.viewmodel

import app.cash.turbine.test
import com.rosahosseini.findr.domain.search.SearchRepository
import com.rosahosseini.findr.feature.search.viewmodel.SearchContract
import com.rosahosseini.findr.feature.search.viewmodel.SearchViewModel
import com.rosahosseini.findr.model.Page
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.state.PagingState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.immutableListOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.jupiter.api.Test

class SearchViewModelTest {
    private val searchRepository: SearchRepository = mockk()
    private val viewModel: SearchViewModel by lazy { SearchViewModel(searchRepository) }

    private val photo = Photo("", null, null, "", null)

    private fun pagedPhoto(items: List<Photo> = emptyList(), pageNumber: Int = 0) = Page(
        items, pageNumber, pageSize = 1, hasNext = true, timeStamp = 0
    )

    @Before
    fun setup() {
        coEvery {
            searchRepository.getRecentPhotos(0, any())
        } returns Result.success(pagedPhoto(pageNumber = 0))
        coEvery {
            searchRepository.getSearchSuggestion(any(), any())
        } returns emptyFlow()
    }


    @Test
    fun `onQueryTextChange_Intent viewModel should update state_term`() = runBlocking {
        // given
        val term = "test"
        coEvery {
            searchRepository.searchPhotos(any(), any(), any())
        } returns Result.success(pagedPhoto(pageNumber = 0))

        // whenever
        viewModel.onIntent(SearchContract.Intent.OnTermChange(term))

        // then
        viewModel.state
            .map { it.term }
            .distinctUntilChanged()
            .test {
                awaitItem() shouldBeEqualTo ""
                awaitItem() shouldBeEqualTo term
            }
    }

    @Test
    fun `onQueryTextChange_Intent viewModel should update state_photos to success`() = runBlocking {
        // given
        val term = "test"
        coEvery {
            searchRepository.searchPhotos(term, 0, any())
        } returns Result.success(pagedPhoto(pageNumber = 0, items = listOf(photo)))

        // whenever
        viewModel.onIntent(SearchContract.Intent.OnTermChange(term))

        // then
        viewModel.state.value.photos shouldBeEqualTo PagingState(
            status = PagingState.Status.Success,
            pageNumber = 0,
            data = persistentListOf(photo),
            exhausted = false,
            throwable = null
        )
    }

    @Test
    fun `onQueryTextChange_Intent viewModel should update state_photos to failure`() = runBlocking {
        // given
        val term = "test"
        val throwable = Throwable("test")
        coEvery {
            searchRepository.searchPhotos(any(), any(), any())
        } returns Result.failure(throwable)

        // whenever
        viewModel.onIntent(SearchContract.Intent.OnTermChange(term))

        // then
        viewModel.state.value.photos shouldBeEqualTo PagingState(
            status = PagingState.Status.Failure,
            pageNumber = 0,
            data = persistentListOf(),
            exhausted = false,
            throwable = throwable
        )
        coVerify { searchRepository.searchPhotos(term, 0, any()) }
    }

    @Test
    fun `On multiple QueryTextChange_Intent viewModel should update photos with last term`() =
        runBlocking {
            // given
            coEvery {
                searchRepository.searchPhotos(any(), any(), any())
            } returns Result.success(pagedPhoto(pageNumber = 0))

            // whenever
            viewModel.onIntent(SearchContract.Intent.OnTermChange("term1"))
            viewModel.onIntent(SearchContract.Intent.OnTermChange("term2"))
            delay(1000)
            viewModel.onIntent(SearchContract.Intent.OnTermChange("term3"))

            // then
            coVerify(exactly = 1) { searchRepository.searchPhotos("term1", 0, any()) }
            coVerify(exactly = 0) { searchRepository.searchPhotos("term2", any(), any()) }
            coVerify(exactly = 1) { searchRepository.searchPhotos("term3", 0, any()) }
        }


    @Test
    fun `On multiple QueryTextChange_Intent viewModel should save last term to history`() =
        runBlocking {
            // given
            coEvery {
                searchRepository.searchPhotos(any(), any(), any())
            } returns Result.success(pagedPhoto(pageNumber = 0))

            // whenever
            viewModel.onIntent(SearchContract.Intent.OnTermChange("term1"))
            viewModel.onIntent(SearchContract.Intent.OnTermChange("term2"))
            delay(1000)
            viewModel.onIntent(SearchContract.Intent.OnTermChange("term3"))

            // then
            coVerify(exactly = 1) { searchRepository.saveSearchQuery("term1") }
            coVerify(exactly = 0) { searchRepository.saveSearchQuery("term2") }
            coVerify(exactly = 1) { searchRepository.saveSearchQuery("term3") }
        }

    @Test
    fun `On QueryTextChange_Intent viewModel should update suggestions`() =
        runBlocking {
            // given
            coEvery {
                searchRepository.searchPhotos(any(), any(), any())
            } returns Result.success(pagedPhoto(pageNumber = 0))
            coEvery {
                searchRepository.getSearchSuggestion("term", any())
            } returns flowOf(listOf("term", "term1", ""), listOf("term3"))

            // whenever
            viewModel.onIntent(SearchContract.Intent.OnTermChange("term"))

            // then
            viewModel.state
                .map { it.suggestions }
                .distinctUntilChanged()
                .test {
                    awaitItem() shouldBeEqualTo immutableListOf()
                    awaitItem() shouldBeEqualTo immutableListOf("tag")
                    awaitItem() shouldBeEqualTo immutableListOf("term3")
                }
            coVerify(exactly = 1) { searchRepository.getSearchSuggestion("term", any()) }
        }


//    @Test
//    fun `onQueryTextChange save query in searchHistory if its not blank`() = runBlocking {
//        // given
//        val query = "query"
//        coEvery {
//            searchRepository.searchPhotos(any(), any(), any())
//        } returns flowOf(Either.Loading())
//
//        // whenever
//        viewModel.onTermChange(query)
//
//        // then
//        coVerify(exactly = 1) { searchRepository.saveSearchQuery(query) }
//    }

//    @Test
//    fun `onQueryTextChange does not save query in searchHistory if it is blank`() = runBlocking {
//        // whenever
//        viewModel.onTermChange("   ")
//        viewModel.onTermChange("")
//
//        // then
//        confirmVerified(searchRepository)
//        coVerify(exactly = 0) { searchRepository.saveSearchQuery(any()) }
//    }

//    @Test
//    fun `onQueryTextChange if query is not null or empty searchPhotos`() = runBlocking {
//        // given
//        val query = "query"
//        coEvery {
//            searchRepository.searchPhotos(any(), eq(0), any())
//        } returns flowOf(Either.Success(pagedPhoto(0)))
//
//
//        // whenever
//        viewModel.onTermChange(query)
//
//        // then
//        coVerify(exactly = 1) { searchRepository.searchPhotos(eq(query), eq(0), any()) }
//    }

//    @Test
//    fun `onLoadMore if last response is success search next page`() = runBlocking {
//        // given
//        val query = "query"
//        repeat(3) { pageNumber ->
//            coEvery {
//                searchRepository.searchPhotos(any(), eq(pageNumber), any())
//            } returns flowOf(Either.Success(pagedPhoto(pageNumber)))
//        }
//
//        // whenever
//        viewModel.onTermChange(query)
//        viewModel.onLoadMorePhotos()
//
//        // then
//        coVerify(exactly = 1) { searchRepository.getRecentPhotos(eq(0), any()) }
//        coVerify(exactly = 1) { searchRepository.searchPhotos(eq(query), eq(1), any()) }
//    }
//
//    @Test
//    fun `onLoadMore if last response is loading does not search anything`() = runBlocking {
//        // given
//        val query = "query"
//        coEvery {
//            searchRepository.searchPhotos(any(), any(), any())
//        } returns flowOf(Either.Loading(pagedPhoto(0)))
//
//        // whenever
//        viewModel.onTermChange(query)
//        viewModel.onLoadMorePhotos()
//
//        // then
//        coVerify(exactly = 1) { searchRepository.getRecentPhotos(eq(0), any()) }
//        coVerify(exactly = 2) { searchRepository.searchPhotos(eq(query), eq(0), any()) }
//    }
//
//    @Test
//    fun `onLoadMore if last response is error research the last page`() = runBlocking {
//        // given
//        val query = "query"
//        coEvery {
//            searchRepository.searchPhotos(any(), any(), any())
//        } returns flowOf(Either.Error(ErrorModel(), pagedPhoto(0)))
//
//        // whenever
//        viewModel.onTermChange(query)
//        viewModel.onLoadMorePhotos()
//
//        // then
//        coVerify(exactly = 1) { searchRepository.getRecentPhotos(eq(0), any()) }
//        coVerify(exactly = 2) { searchRepository.searchPhotos(eq(query), eq(0), any()) }
//    }
//
//    @Test
//    fun `onCreate getRecentPhotos`() = runBlocking {
//        // whenever
//        viewModel // init viewmodel
//
//        // then
//        coVerify(exactly = 1) { searchRepository.getRecentPhotos(eq(0), any()) }
//    }
//
//    @Test
//    fun `onCancelSearchSuggestion would call removeSuggestion`() = runBlocking {
//        // whenever
//        viewModel.onCancelSearchSuggestion(suggestionModel)
//
//        // then
//        coVerify(exactly = 1) { searchRepository.removeSearchQuery(suggestionModel.tag) }
//    }
//
//    @Test
//    fun `searchedPhotos is fetched from searchLocalPhotos`() = runBlocking {
//        // given
//        val localPhotos = listOf(photo, photo, photo)
//        coEvery {
//            searchRepository.searchLocalPhotos(any(), any(), any(), any())
//        } returns flowOf(localPhotos)
//
//        // whenever {
//        val actual = viewModel.searchedPhotos.first()
//
//        // then
//        assertEquals(actual, localPhotos)
//    }
//
//    @Test
//    fun `searchSuggestions is fetched from get`() = runTest {
//        // given
//        val expected = listOf(suggestionModel, suggestionModel, suggestionModel)
//        coEvery {
//            searchRepository.getSearchSuggestion(any(), any())
//        } returns flowOf(expected.map { it.tag })
//
//        // whenever
//        val actual = viewModel.searchSuggestions.first()
//
//        // then
//        assertEquals(expected, actual)
//    }
}