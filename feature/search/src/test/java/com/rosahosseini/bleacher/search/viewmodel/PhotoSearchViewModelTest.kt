package com.rosahosseini.bleacher.search.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.rosahosseini.bleacher.commontest.CoroutineTestRule
import com.rosahosseini.bleacher.commontest.coroutineTestCase
import com.rosahosseini.bleacher.model.Either
import com.rosahosseini.bleacher.model.ErrorModel
import com.rosahosseini.bleacher.model.Page
import com.rosahosseini.bleacher.model.Photo
import com.rosahosseini.bleacher.navigation.Navigator
import com.rosahosseini.bleacher.navigation.destinations.BookmarkDestination
import com.rosahosseini.bleacher.navigation.destinations.PhotoDetailDestination
import com.rosahosseini.bleacher.repository.BookmarkRepository
import com.rosahosseini.bleacher.repository.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoSearchViewModelTest {
    private val bookmarkRepository: BookmarkRepository = mock()
    private val searchRepository: SearchRepository = mock()
    private val navigator: Navigator = mock()
    private val viewModel: PhotoSearchViewModel by lazy {
        PhotoSearchViewModel(
            searchRepository,
            bookmarkRepository,
            navigator
        )
    }

    private val photo = Photo(
        id = "", isBookmarked = false, title = null, description = null, urlOriginal = null,
        urlLargeNullable = null, urlMedium800px = null, urlMedium640px = null, urlSmall320px = null,
        urlSmall240px = null, urlThumbnail150px = null, urlThumbnail100px = null,
        urlThumbnail75px = null, urlThumbnailSquare = null
    )

    private fun pagedPhoto(pageNumber: Int) = Page(
        listOf(photo, photo), pageNumber, pageSize = 2, hasNext = true, timeStamp = 0
    )

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        runTest {
            whenever(
                searchRepository.getRecentPhotos(any(), any())
            ) doReturn flowOf(Either.Loading())
            whenever(
                searchRepository.searchLocalPhotos(any(), any(), any(), any())
            ) doReturn flowOf(listOf(photo, photo))
        }
    }

    @Test
    fun `onPhotoClick would navigate to photo detail`() = coroutineTestCase {
        whenever {
            viewModel.onPhotoClick(photo)
        }
        then {
            verify(navigator, times(1)).navigateTo(
                eq(PhotoDetailDestination),
                eq(listOf(Pair("photo", photo)))
            )
        }
    }

    @Test
    fun `onToggleBookmark would change bookmark state from bookmarkRepository`() =
        coroutineTestCase {
            val photo1 = photo.copy(id = "1", isBookmarked = false)
            val photo2 = photo.copy(id = "2", isBookmarked = true)
            whenever {
                viewModel.onToggleBookmark(photo1)
                viewModel.onToggleBookmark(photo2)
            }
            then {
                verify(bookmarkRepository).changeBookmarkState(photo1.id, true)
                verify(bookmarkRepository).changeBookmarkState(photo2.id, false)
            }
        }

    @Test
    fun `onBookmarksClick would navigate to bookmark destination `() = coroutineTestCase {
        whenever {
            viewModel.onBookmarksClick()
        }
        then {
            verify(navigator).navigateTo(BookmarkDestination)
        }
    }

    @Test
    fun `onQueryTextChange if query is empty getRecentPhotos`() = coroutineTestCase {
        whenever {
            viewModel.onQueryTextChange("")
        }
        then {
            verify(searchRepository, times(2)).getRecentPhotos(eq(0), any())
            verifyNoMoreInteractions(searchRepository)
        }
    }

    @Test
    fun `onQueryTextChange if query is not null or empty searchPhotos`() = coroutineTestCase {
        val query = "query"
        given {
            whenever(
                searchRepository.searchPhotos(any(), eq(0), any())
            ) doReturn (flowOf(Either.Success(pagedPhoto(0))))
        }
        whenever {
            viewModel.onQueryTextChange(query)
        }
        then {
            verify(searchRepository).searchPhotos(eq(query), eq(0), any())
        }
    }

    @Test
    fun `onLoadMore if last response is success search next page`() = coroutineTestCase {
        val query = "query"
        given {
            whenever(
                searchRepository.searchPhotos(any(), eq(0), any())
            ) doReturn (flowOf(Either.Success(pagedPhoto(0))))
            whenever(
                searchRepository.searchPhotos(any(), eq(1), any())
            ) doReturn (flowOf(Either.Success(pagedPhoto(1))))
            whenever(
                searchRepository.searchPhotos(any(), eq(2), any())
            ) doReturn (flowOf(Either.Success(pagedPhoto(2))))
        }
        whenever {
            viewModel.onQueryTextChange(query)
            viewModel.onLoadMore()
        }
        then {
            verify(searchRepository, times(1)).getRecentPhotos(eq(0), any())
            verify(searchRepository, times(1)).searchPhotos(eq(query), eq(0), any())
            verify(searchRepository, times(1)).searchPhotos(eq(query), eq(1), any())
            verifyNoMoreInteractions(searchRepository)
        }
    }

    @Test
    fun `onLoadMore if last response is loading does not search anything`() = coroutineTestCase {
        val query = "query"
        given {
            whenever(
                searchRepository.searchPhotos(any(), any(), any())
            ) doReturn (flowOf(Either.Loading(pagedPhoto(0))))
        }
        whenever {
            viewModel.onQueryTextChange(query)
            viewModel.onLoadMore()
        }
        then {
            verify(searchRepository, times(1)).getRecentPhotos(eq(0), any())
            verify(searchRepository, times(1)).searchPhotos(eq(query), eq(0), any())
            verifyNoMoreInteractions(searchRepository)
        }
    }

    @Test
    fun `onLoadMore if last response is error research the last page`() = coroutineTestCase {
        val query = "query"
        given {
            whenever(
                searchRepository.searchPhotos(any(), any(), any())
            ) doReturn (flowOf(Either.Error(ErrorModel(), pagedPhoto(0))))
        }
        whenever {
            viewModel.onQueryTextChange(query)
            viewModel.onLoadMore()
        }
        then {
            verify(searchRepository, times(1)).getRecentPhotos(eq(0), any())
            verify(searchRepository, times(2)).searchPhotos(eq(query), eq(0), any())
            verifyNoMoreInteractions(searchRepository)
        }
    }

    @Test
    fun `onCreate getRecentPhotos`() = coroutineTestCase {
        whenever {
            viewModel // init viewmodel
        }
        then {
            verify(searchRepository, times(1)).getRecentPhotos(eq(0), any())
            verifyNoMoreInteractions(searchRepository)
        }
    }
}