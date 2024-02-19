package com.rosahosseini.findr.bookmark.viewmodel

import com.rosahosseini.findr.feature.bookmark.viewmodel.BookmarkViewModel
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.repository.BookmarkRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.jupiter.api.Test

class BookmarkViewModelTest {
    private val bookmarkRepository: BookmarkRepository = mockk()
    private val viewModel: BookmarkViewModel by lazy { BookmarkViewModel(bookmarkRepository) }


    private val photo = Photo(
        id = "", isBookmarked = false, title = null, description = null, urlOriginal = "",
        urlLargeNullable = null, urlMedium800px = null, urlMedium640px = null, urlSmall320px = null,
        urlSmall240px = null, urlThumbnail150px = null, urlThumbnail100px = null,
        urlThumbnail75px = null, urlThumbnailSquare = null
    )

    @Before
    fun setUp() {
        every { bookmarkRepository.getAllBookmarkedPhotos() } returns flowOf(emptyList())
    }

    @Test
    fun `onToggleBookmark change bookmark start by repository`() = runTest {

        // given
        val photo1 = photo.copy(id = "1", isBookmarked = false)
        val photo2 = photo.copy(id = "2", isBookmarked = true)

        // whenever
        viewModel.onToggleBookmark(photo1)
        viewModel.onToggleBookmark(photo2)

        // then
        coVerify(exactly = 1) {
            bookmarkRepository.changeBookmarkState(photo1.id, true)
        }
        coVerify(exactly = 1) {
            bookmarkRepository.changeBookmarkState(photo2.id, false)
        }
    }
}