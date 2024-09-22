package com.rosahosseini.findr.feature.bookmark.viewmodel

import com.rosahosseini.findr.domain.bookmark.BookmarkRepository
import com.rosahosseini.findr.domain.model.Photo
import com.rosahosseini.findr.ui.state.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBeEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
class BookmarkViewModelTest {

    private val bookmarkRepository: BookmarkRepository = mockk()
    private val viewModel: BookmarkViewModel by lazy { BookmarkViewModel(bookmarkRepository) }
    private val photo = Photo("1", null, null, "", null)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        every { bookmarkRepository.getAllBookmarkedPhotos() } returns flowOf(emptyList())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `OnUpdateBookmarkIntent calls changeBookmarkState from bookmarkRepository`() = runTest {
        // given
        coEvery { bookmarkRepository.changeBookmarkState(any(), any()) } returns mockk()

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
        viewModel.state.value.photos shouldBeEqualTo UiState.Success(
            bookmarkedPhotos.toImmutableList()
        )
        coVerify(exactly = 1) { bookmarkRepository.getAllBookmarkedPhotos() }
    }

    @Test
    fun `OnInit viewModel sets bookmarks state`() = runTest {
        // given
        val bookmarkedPhotos = listOf(photo.copy("1"))
        every { bookmarkRepository.getAllBookmarkedPhotos() } returns flowOf(bookmarkedPhotos)

        // then
        viewModel.state.value.bookmarks shouldBeEqualTo persistentHashMapOf("1" to true)
        coVerify(exactly = 1) { bookmarkRepository.getAllBookmarkedPhotos() }
    }
}
