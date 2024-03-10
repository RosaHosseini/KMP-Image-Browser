import com.rosahosseini.findr.data.search.local.SearchHistoryLocalDataSource
import com.rosahosseini.findr.data.search.remote.datasource.PhotoRemoteDataSource
import com.rosahosseini.findr.data.search.remote.response.toPagePhotos
import com.rosahosseini.findr.data.search.repository.DefaultSearchRepository
import com.rosahosseini.findr.db.entity.SearchHistoryEntity
import com.rosahosseini.findr.domain.search.SearchRepository
import com.rosahosseini.findr.library.coroutines.CoroutineDispatchers
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class SearchRepositoryTest {

    private val photoRemoteDataSource: PhotoRemoteDataSource = mockk()
    private val searchHistoryLocalDataSource: SearchHistoryLocalDataSource = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val coroutineDispatchers = CoroutineDispatchers(
        main = UnconfinedTestDispatcher(),
        default = UnconfinedTestDispatcher(),
        io = UnconfinedTestDispatcher()
    )

    private val repository: SearchRepository by lazy {
        DefaultSearchRepository(
            photoRemoteDataSource,
            searchHistoryLocalDataSource,
            coroutineDispatchers
        )
    }

    @Test
    fun `on searchPhotos gets data from photoRemoteDataSource and returns success`() = runTest {
        // Given
        coEvery {
            photoRemoteDataSource.search(any(), any(), any())
        } returns Result.success(searchDto)

        // When
        val actual = repository.searchPhotos(query = "query", pageNumber = 0, pageSize = 5)

        // Then
        coVerify(exactly = 1) { photoRemoteDataSource.search(text = "query", limit = 5, page = 0) }
        confirmVerified(photoRemoteDataSource, photoRemoteDataSource)
        actual shouldBeEqualTo Result.success(searchDto.toPagePhotos())
    }

    @Test
    fun `on searchPhotos gets data from photoRemoteDataSource and returns failure`() = runTest {
        // Given
        val throwable = Throwable("test")
        coEvery {
            photoRemoteDataSource.search(any(), any(), any())
        } returns Result.failure(throwable)

        // When
        val actual = repository.searchPhotos(query = "query", pageNumber = 0, pageSize = 5)

        // Then
        coVerify(exactly = 1) { photoRemoteDataSource.search(text = "query", limit = 5, page = 0) }
        confirmVerified(photoRemoteDataSource, photoRemoteDataSource)
        actual shouldBeEqualTo Result.failure(throwable)
    }

    @Test
    fun `on getRecentPhotos gets data from photoRemoteDataSource and returns success`() = runTest {
        // Given
        coEvery {
            photoRemoteDataSource.getRecent(any(), any())
        } returns Result.success(searchDto)

        // When
        val actual = repository.getRecentPhotos(pageNumber = 0, pageSize = 5)

        // Then
        coVerify(exactly = 1) { photoRemoteDataSource.getRecent(limit = 5, page = 0) }
        confirmVerified(photoRemoteDataSource, photoRemoteDataSource)
        actual shouldBeEqualTo Result.success(searchDto.toPagePhotos())
    }

    @Test
    fun `on getRecentPhotos gets data from photoRemoteDataSource and returns failure`() = runTest {
        // Given
        val throwable = Throwable("test")
        coEvery {
            photoRemoteDataSource.getRecent(any(), any())
        } returns Result.failure(throwable)

        // When
        val actual = repository.getRecentPhotos(pageNumber = 0, pageSize = 5)

        // Then
        coVerify(exactly = 1) { photoRemoteDataSource.getRecent(limit = 5, page = 0) }
        confirmVerified(photoRemoteDataSource, photoRemoteDataSource)
        actual shouldBeEqualTo Result.failure(throwable)
    }


    @Test
    fun `on clearExpiredData clear data by local data source`() = runBlocking {
        // Given
        val expireTime = 10L
        coEvery { searchHistoryLocalDataSource.clearExpiredQueries(expireTime) } just runs

        // When
        repository.clearExpiredData(expireTime)

        // Then
        coVerify(exactly = 1) { searchHistoryLocalDataSource.clearExpiredQueries(expireTime) }
        confirmVerified(photoRemoteDataSource, photoRemoteDataSource)
    }

    @Test
    fun `on saveSearchQuery save query in search history local datasource`() = runBlocking {
        // Given
        coEvery { searchHistoryLocalDataSource.saveQuery("query") } just runs

        // When
        repository.saveSearchQuery("query")

        // Then
        coVerify(exactly = 1) { searchHistoryLocalDataSource.saveQuery("query") }
        confirmVerified(photoRemoteDataSource, searchHistoryLocalDataSource)
    }

    @Test
    fun `on removeQuery remove query from search history local datasource`() = runBlocking {
        // Given
        coEvery { searchHistoryLocalDataSource.removeQuery("query") } just runs

        // When
        repository.removeSearchQuery("query")

        // Then
        coVerify(exactly = 1) { searchHistoryLocalDataSource.removeQuery("query") }
        confirmVerified(photoRemoteDataSource, searchHistoryLocalDataSource)
    }

    @Test
    fun `on getSearchSuggestion get recent queries from search history local datasource`() =
        runBlocking {
            // Given

            val searchSuggestion = SearchHistoryEntity("", 1)
            coEvery {
                searchHistoryLocalDataSource.getLatestQueries(any(), any())
            } returns flowOf(listOf(searchSuggestion, searchSuggestion))

            // When
            repository.getSearchSuggestion(query = "query", limit = 4)

            // Then
            verify(exactly = 1) {
                searchHistoryLocalDataSource.getLatestQueries(query = "query", limit = 4)
            }
            confirmVerified(photoRemoteDataSource, searchHistoryLocalDataSource)
        }
}