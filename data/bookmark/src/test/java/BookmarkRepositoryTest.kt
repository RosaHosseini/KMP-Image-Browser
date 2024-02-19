import app.cash.turbine.test
import com.rosahosseini.findr.domain.bookmark.BookmarkRepository
import com.rosahosseini.findr.data.bookmark.repository.DefaultBookmarkRepository
import com.rosahosseini.findr.db.entity.PhotoEntity
import com.rosahosseini.findr.db.entity.toPhoto
import com.rosahosseini.findr.data.bookmark.local.BookmarkLocalDataSource
import com.rosahosseini.findr.model.Either
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class BookmarkRepositoryTest {
    private val bookmarkLocalDataSource: BookmarkLocalDataSource = mockk()
    private val repository: BookmarkRepository by lazy {
        DefaultBookmarkRepository(bookmarkLocalDataSource)
    }

    private val photo = PhotoEntity(
        photoId = "",
        isBookmarked = false,
        title = null,
        description = null,
        url = "",
        thumbnailUrl = null,
        timeStamp = 0
    )


    @Test
    fun `on changeBookmarkState change bookmark state in db`() = runTest {
        // given
        val photoId = ""
        val isBookmark = false
        // whenever
        repository.changeBookmarkState(photoId, isBookmark)
        // then
        coVerify(exactly = 1) { bookmarkLocalDataSource.changeBookmarkState(photoId, isBookmark) }
    }

    @Test
    fun `on changeBookmarkState return EitherError if exception happen`() = runTest {
        // given
        coEvery {
            bookmarkLocalDataSource.changeBookmarkState(any(), any())
        } throws RuntimeException("")

        // whenever
        val actual = repository.changeBookmarkState("", false)

        // then
        assert(actual is Either.Error)
    }

    @Test
    fun `on changeBookmarkState return EitherSuccess if exception not happens`() = runBlocking {
        // whenever
        val actual = repository.changeBookmarkState("", false)

        // then
        assert(actual is Either.Success)
    }

    @Test
    fun ` getAllBookmarkedPhotos would call local datasource`() = runBlocking {
        // given
        val photos = listOf(photo, photo)
        every { bookmarkLocalDataSource.getBookmarkedPhotos() } returns flowOf(photos)

        // whenever
        val actual = repository.getAllBookmarkedPhotos()

        // then
        verify { bookmarkLocalDataSource.getBookmarkedPhotos() }
        actual.test {
            awaitItem() shouldBeEqualTo photos.map { it.toPhoto() }
            awaitComplete()
        }
    }
}