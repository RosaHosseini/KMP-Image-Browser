package com.rosahosseini.findr.bookmark.viewmodel

import app.cash.turbine.test
import com.rosahosseini.findr.domain.bookmark.BookmarkRepository
import com.rosahosseini.findr.feature.bookmark.viewmodel.BookmarkContract
import com.rosahosseini.findr.feature.bookmark.viewmodel.BookmarkViewModel
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.state.UiState
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.immutableHashMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class BookmarkViewModelTest {

    private val bookmarkRepository: BookmarkRepository = mockk()
    private val viewModel: BookmarkViewModel by lazy { BookmarkViewModel(bookmarkRepository) }
    private val photo = Photo("1", null, null, "", null)

    @Test
    fun `OnUpdateBookmarkIntent calls changeBookmarkState from bookmarkRepository`() = runTest {
        // given
        every { bookmarkRepository.getAllBookmarkedPhotos() } returns flowOf(emptyList())

        // whenever
        viewModel.onIntent(BookmarkContract.Intent.OnUpdateBookmark(photo, enabled = false))

        // then
        coVerify(exactly = 1) { bookmarkRepository.changeBookmarkState(photo, false) }
    }

    @Test
    fun `OnInit viewModel sets photos state`() = runTest {
        // given
        val bookmarkedPhotos = listOf(photo, photo)
        every { bookmarkRepository.getAllBookmarkedPhotos() } returns flowOf(bookmarkedPhotos)

        // then
        viewModel.state
            .map { it.photos }
            .distinctUntilChanged()
            .test {
                awaitItem() shouldBeEqualTo UiState.Idle(null)
                awaitItem() shouldBeEqualTo UiState.Loading(null)
                awaitItem() shouldBeEqualTo UiState.Success(bookmarkedPhotos.toImmutableList())
                awaitComplete()
            }
        coVerify(exactly = 1) { bookmarkRepository.getAllBookmarkedPhotos() }
    }

    @Test
    fun `OnInit viewModel sets bookmarks state`() = runTest {
        // given
        val bookmarkedPhotos = listOf(photo.copy("1"))
        every { bookmarkRepository.getAllBookmarkedPhotos() } returns flowOf(bookmarkedPhotos)

        // then
        viewModel.state
            .map { it.bookmarks }
            .distinctUntilChanged()
            .test {
                awaitItem() shouldBeEqualTo immutableHashMapOf()
                awaitItem() shouldBeEqualTo immutableHashMapOf("1" to true)
                awaitItem() shouldBeEqualTo immutableHashMapOf()
            }
        coVerify(exactly = 1) { bookmarkRepository.getAllBookmarkedPhotos() }
    }
}