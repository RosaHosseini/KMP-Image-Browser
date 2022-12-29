package com.rosahosseini.findr.bookmark.viewmodel

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rosahosseini.findr.commontest.CoroutineTestRule
import com.rosahosseini.findr.commontest.coroutineTestCase
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.navigation.Navigator
import com.rosahosseini.findr.navigation.destinations.PhotoDetailDestination
import com.rosahosseini.findr.repository.BookmarkRepository
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BookmarkViewModelTest {
    private val bookmarkRepository: BookmarkRepository = mock()
    private val navigator: Navigator = mock()
    private val viewModel: BookmarkViewModel by lazy {
        BookmarkViewModel(
            bookmarkRepository,
            navigator
        )
    }

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private val photo = Photo(
        id = "", isBookmarked = false, title = null, description = null, urlOriginal = null,
        urlLargeNullable = null, urlMedium800px = null, urlMedium640px = null, urlSmall320px = null,
        urlSmall240px = null, urlThumbnail150px = null, urlThumbnail100px = null,
        urlThumbnail75px = null, urlThumbnailSquare = null
    )

    @Before
    fun setUp() {
        whenever(bookmarkRepository.getAllBookmarkedPhotos()) doReturn flowOf(emptyList())
    }

    @Test
    fun `onToggleBookmark change bookmark start by repository`() = coroutineTestCase {
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
}