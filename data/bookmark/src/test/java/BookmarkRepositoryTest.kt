import app.cash.turbine.test
import com.rosahosseini.findr.data.bookmark.local.BookmarkLocalDataSource
import com.rosahosseini.findr.data.bookmark.repository.DefaultBookmarkRepository
import com.rosahosseini.findr.db.entity.PhotoEntity
import com.rosahosseini.findr.db.entity.toPhoto
import com.rosahosseini.findr.domain.bookmark.BookmarkRepository
import com.rosahosseini.findr.library.coroutines.CoroutineDispatchers
import com.rosahosseini.findr.model.Photo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class BookmarkRepositoryTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val coroutineDispatchers = CoroutineDispatchers(
        main = UnconfinedTestDispatcher(),
        default = UnconfinedTestDispatcher(),
        io = UnconfinedTestDispatcher()
    )
    private val bookmarkLocalDataSource: BookmarkLocalDataSource = mockk()
    private val repository: BookmarkRepository by lazy {
        DefaultBookmarkRepository(bookmarkLocalDataSource, coroutineDispatchers)
    }

    private val photoEntity = PhotoEntity(
        photoId = "",
        isBookmarked = false,
        title = null,
        description = null,
        url = "",
        thumbnailUrl = null,
        timeStamp = 0
    )

    private val photo = Photo(
        id = "", title = null, description = null, url = "", thumbnailUrl = null
    )


    @Test
    fun `on changeBookmarkState change bookmark state in db and return success`() {
        runBlocking {
            // given
            coEvery { bookmarkLocalDataSource.changeBookmarkState(any()) } just runs
            // whenever
            val result = repository.changeBookmarkState(photo, true)
            // then
            coVerify(exactly = 1) { bookmarkLocalDataSource.changeBookmarkState(any()) }
            result shouldBeEqualTo Result.success(Unit)
        }
    }

    @Test
    fun `on changeBookmarkState change bookmark state in db and return error if it throws`() {
        runBlocking {
            // given
            val throwable = mockk<Throwable>()
            coEvery { bookmarkLocalDataSource.changeBookmarkState(any()) } throws throwable
            // whenever
            val result = repository.changeBookmarkState(photo, true)
            // then
            coVerify(exactly = 1) {
                bookmarkLocalDataSource.changeBookmarkState(any())
            }
            result shouldBeEqualTo Result.failure(throwable)
        }
    }

    @Test
    fun ` getAllBookmarkedPhotos would call local datasource`() = runBlocking {
        // given
        val photos = listOf(photoEntity, photoEntity)
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