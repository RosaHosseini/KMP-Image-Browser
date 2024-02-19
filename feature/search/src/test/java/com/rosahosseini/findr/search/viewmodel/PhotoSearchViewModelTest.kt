package com.rosahosseini.findr.search.viewmodel

import com.rosahosseini.findr.model.Either
import com.rosahosseini.findr.model.ErrorModel
import com.rosahosseini.findr.model.Page
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.repository.BookmarkRepository
import com.rosahosseini.findr.repository.SearchRepository
import com.rosahosseini.findr.feature.search.model.SuggestionModel
import com.rosahosseini.findr.feature.search.viewmodel.PhotoSearchViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PhotoSearchViewModelTest {
    private val bookmarkRepository: BookmarkRepository = mockk()
    private val searchRepository: SearchRepository = mockk()
    private val viewModel: PhotoSearchViewModel by lazy {
        PhotoSearchViewModel(
            searchRepository,
            bookmarkRepository
        )
    }

    private val suggestionModel = SuggestionModel("tag")

    private val photo = Photo(
        id = "", isBookmarked = false, title = null, description = null, urlOriginal = "",
        urlLargeNullable = null, urlMedium800px = null, urlMedium640px = null, urlSmall320px = null,
        urlSmall240px = null, urlThumbnail150px = null, urlThumbnail100px = null,
        urlThumbnail75px = null, urlThumbnailSquare = null
    )

    private fun pagedPhoto(pageNumber: Int) = Page(
        listOf(photo, photo), pageNumber, pageSize = 2, hasNext = true, timeStamp = 0
    )

    @Before
    fun setup() {
        coEvery {
            searchRepository.getRecentPhotos(any(), any())
        } returns flowOf(Either.Loading())
        coEvery {
            searchRepository.searchLocalPhotos(any(), any(), any(), any())
        } returns flowOf(listOf(photo, photo))
    }

    @Test
    fun `onToggleBookmark would change bookmark state from bookmarkRepository`() = runTest {

        // given
        val photo1 = photo.copy(id = "1", isBookmarked = false)
        val photo2 = photo.copy(id = "2", isBookmarked = true)

        // whenever
        viewModel.onToggleBookmark(photo1)
        viewModel.onToggleBookmark(photo2)

        // then
        coVerify(exactly = 1) { bookmarkRepository.changeBookmarkState(photo1.id, true) }
        coVerify(exactly = 1) { bookmarkRepository.changeBookmarkState(photo2.id, false) }
    }


    @Test
    fun `onQueryTextChange if query is empty getRecentPhotos`() = runBlocking {
        // whenever
        viewModel.onQueryTextChange("")

        // then
        coVerify(exactly = 2) { searchRepository.getRecentPhotos(eq(0), any()) }
    }

    @Test
    fun `onQueryTextChange save query in searchHistory if its not blank`() = runBlocking {
        // given
        val query = "query"
        coEvery {
            searchRepository.searchPhotos(any(), any(), any())
        } returns flowOf(Either.Loading())

        // whenever
        viewModel.onQueryTextChange(query)

        // then
        coVerify(exactly = 1) { searchRepository.saveSearchQuery(query) }
    }

    @Test
    fun `onQueryTextChange does not save query in searchHistory if it is blank`() = runBlocking {
        // whenever
        viewModel.onQueryTextChange("   ")
        viewModel.onQueryTextChange("")

        // then
        confirmVerified(searchRepository)
        coVerify(exactly = 0) { searchRepository.saveSearchQuery(any()) }
    }

    @Test
    fun `onQueryTextChange if query is not null or empty searchPhotos`() = runBlocking {
        // given
        val query = "query"
        coEvery {
            searchRepository.searchPhotos(any(), eq(0), any())
        } returns flowOf(Either.Success(pagedPhoto(0)))


        // whenever
        viewModel.onQueryTextChange(query)

        // then
        coVerify(exactly = 1) { searchRepository.searchPhotos(eq(query), eq(0), any()) }
    }

    @Test
    fun `onLoadMore if last response is success search next page`() = runBlocking {
        // given
        val query = "query"
        repeat(3) { pageNumber ->
            coEvery {
                searchRepository.searchPhotos(any(), eq(pageNumber), any())
            } returns flowOf(Either.Success(pagedPhoto(pageNumber)))
        }

        // whenever
        viewModel.onQueryTextChange(query)
        viewModel.onLoadMore()

        // then
        coVerify(exactly = 1) { searchRepository.getRecentPhotos(eq(0), any()) }
        coVerify(exactly = 1) { searchRepository.searchPhotos(eq(query), eq(1), any()) }
    }

    @Test
    fun `onLoadMore if last response is loading does not search anything`() = runBlocking {
        // given
        val query = "query"
        coEvery {
            searchRepository.searchPhotos(any(), any(), any())
        } returns flowOf(Either.Loading(pagedPhoto(0)))

        // whenever
        viewModel.onQueryTextChange(query)
        viewModel.onLoadMore()

        // then
        coVerify(exactly = 1) { searchRepository.getRecentPhotos(eq(0), any()) }
        coVerify(exactly = 2) { searchRepository.searchPhotos(eq(query), eq(0), any()) }
    }

    @Test
    fun `onLoadMore if last response is error research the last page`() = runBlocking {
        // given
        val query = "query"
        coEvery {
            searchRepository.searchPhotos(any(), any(), any())
        } returns flowOf(Either.Error(ErrorModel(), pagedPhoto(0)))

        // whenever
        viewModel.onQueryTextChange(query)
        viewModel.onLoadMore()

        // then
        coVerify(exactly = 1) { searchRepository.getRecentPhotos(eq(0), any()) }
        coVerify(exactly = 2) { searchRepository.searchPhotos(eq(query), eq(0), any()) }
    }

    @Test
    fun `onCreate getRecentPhotos`() = runBlocking {
        // whenever
        viewModel // init viewmodel

        // then
        coVerify(exactly = 1) { searchRepository.getRecentPhotos(eq(0), any()) }
    }

    @Test
    fun `onCancelSearchSuggestion would call removeSuggestion`() = runBlocking {
        // whenever
        viewModel.onCancelSearchSuggestion(suggestionModel)

        // then
        coVerify(exactly = 1) { searchRepository.removeSuggestion(suggestionModel.tag) }
    }

    @Test
    fun `searchedPhotos is fetched from searchLocalPhotos`() = runBlocking {
        // given
        val localPhotos = listOf(photo, photo, photo)
        coEvery {
            searchRepository.searchLocalPhotos(any(), any(), any(), any())
        } returns flowOf(localPhotos)

        // whenever {
        val actual = viewModel.searchedPhotos.first()

        // then
        assertEquals(actual, localPhotos)
    }

    @Test
    fun `searchSuggestions is fetched from get`() = runTest {
        // given
        val expected = listOf(suggestionModel, suggestionModel, suggestionModel)
        coEvery {
            searchRepository.getSearchSuggestion(any(), any())
        } returns flowOf(expected.map { it.tag })

        // whenever
        val actual = viewModel.searchSuggestions.first()

        // then
        assertEquals(expected, actual)
    }
}