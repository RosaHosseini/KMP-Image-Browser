package com.rosahosseini.findr.bookmark.viewmodel

import com.rosahosseini.findr.domain.bookmark.BookmarkRepository
import com.rosahosseini.findr.feature.bookmark.viewmodel.BookmarkViewModel
import com.rosahosseini.findr.model.Photo
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


    private val photo = Photo("", false, null, null, "", null)

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