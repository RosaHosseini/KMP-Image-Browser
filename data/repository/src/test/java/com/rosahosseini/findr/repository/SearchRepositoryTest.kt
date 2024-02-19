package com.rosahosseini.findr.repository

import app.cash.turbine.test
import com.rosahosseini.findr.local.database.entity.SearchedPhotoEntity
import com.rosahosseini.findr.local.datasource.SearchHistoryLocalDataSource
import com.rosahosseini.findr.local.datasource.SearchPhotoLocalDataSource
import com.rosahosseini.findr.model.Either
import com.rosahosseini.findr.remote.datasource.PhotoRemoteDataSource
import com.rosahosseini.findr.remote.model.response.PhotoDto
import com.rosahosseini.findr.repository.impl.SearchRepositoryImpl
import com.rosahosseini.findr.repository.map.toPagePhotos
import com.rosahosseini.findr.repository.map.toPhoto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class SearchRepositoryTest {
    private val photoRemoteDataSource: PhotoRemoteDataSource = mockk()
    private val searchLocalDataSource: SearchPhotoLocalDataSource = mockk()
    private val searchHistoryLocalDataSource: SearchHistoryLocalDataSource = mockk()

    private val repository: SearchRepository by lazy {
        SearchRepositoryImpl(
            photoRemoteDataSource,
            searchLocalDataSource,
            searchHistoryLocalDataSource
        )
    }

    @Test
    fun `on searchPhotos gets data from db if it isn't outdated`() = runTest {
        // given
        val photoList = listOf(searchedPhotoEntity, searchedPhotoEntity)
        coEvery { searchLocalDataSource.search(any(), any(), any()) } returns photoList

        // whenever
        val actual = repository.searchPhotos("query", 0, 5)

        // then
        actual.test {
            awaitItem() shouldBeEqualTo Either.Loading()
            awaitItem() shouldBeEqualTo Either.Success(photoList.toPagePhotos(5))
            awaitComplete()
        }
    }

    @Test
    fun `on searchPhotos gets data from remote if db data is outdated`() = runTest {
        // given
        val photoList = listOf(outdatedSearchedPhotoEntity, outdatedSearchedPhotoEntity)
        coEvery { searchLocalDataSource.search(any(), any(), any()) } returns photoList
        coEvery { photoRemoteDataSource.search(any(), any(), any()) } returns searchResponse

        // whenever
        val actual = repository.searchPhotos("query", 0, 5)

        // then
        actual.test {
            awaitItem() shouldBeEqualTo Either.Loading()
            awaitItem() shouldBeEqualTo Either.Loading(photoList.toPagePhotos(5))
            awaitItem() shouldBeEqualTo Either.Success(searchResponse.toPagePhotos())
            awaitComplete()
        }
        coVerify { searchLocalDataSource.search(any(), any(), any()) }
        coVerify { searchLocalDataSource.search(any(), any(), any()) }
        confirmVerified(searchLocalDataSource)
        confirmVerified(searchLocalDataSource)
    }

    @Test
    fun `on searchPhotos gets data from remote if db data is empty`() = runBlocking {
        // given
        coEvery { searchLocalDataSource.search(any(), any(), any()) } returns emptyList()
        coEvery { photoRemoteDataSource.search(any(), any(), any()) } returns searchResponse

        // whenever
        val actual = repository.searchPhotos("query", 0, 5)

        // then
        actual.test {
            awaitItem() shouldBeEqualTo Either.Loading()
            awaitItem().let {
                it.data?.items shouldBeEqualTo searchResponse.data.photos?.map(PhotoDto::toPhoto)
                    .orEmpty()
                it shouldBeInstanceOf Either.Success::class
            }
            awaitComplete()
        }
        coVerify { searchLocalDataSource.search(any(), any(), any()) }
        coVerify { searchLocalDataSource.search(any(), any(), any()) }
        confirmVerified(searchLocalDataSource)
        confirmVerified(searchLocalDataSource)
    }

    @Test
    fun `on searchPhotos gets data db again if remote throw exception`() = runTest {
        // given
        coEvery { searchLocalDataSource.search(any(), any(), any()) } returns emptyList()
        coEvery { photoRemoteDataSource.search(any(), any(), any()) } throws Exception("test")

        // whenever
        val actual = repository.searchPhotos("query", 0, 5)

        // then
        actual.test {
            awaitItem() shouldBeInstanceOf Either.Loading::class
            awaitItem() shouldBeEqualTo Either.Loading(
                emptyList<SearchedPhotoEntity>().toPagePhotos(5)
            )
            awaitItem() shouldBeInstanceOf Either.Success::class
            awaitComplete()
        }
        coVerify(exactly = 2) { searchLocalDataSource.search(any(), any(), any()) }
        coVerify(exactly = 1) { searchLocalDataSource.search(any(), any(), any()) }
        confirmVerified(searchLocalDataSource)
        confirmVerified(searchLocalDataSource)
    }

    @Test
    fun `on getRecentPhotos gets data from db if it isn't outdated`() = runTest {
        // given
        val photoList = listOf(searchedPhotoEntity, searchedPhotoEntity)
        coEvery { searchLocalDataSource.search(null, any(), any()) } returns photoList

        // whenever
        val actual = repository.searchPhotos("query", 0, 5)

        // then
        actual.test {
            awaitItem() shouldBeInstanceOf Either.Loading::class
            awaitItem() shouldBeEqualTo Either.Success::class
            awaitComplete()
        }
        coVerify(exactly = 2) { searchLocalDataSource.search(null, any(), any()) }
        confirmVerified(searchLocalDataSource)
        confirmVerified(searchLocalDataSource)
    }

    @Test
    fun `on getRecentPhotos gets data from remote if db data is outdated`() = runTest {
        // given
        val photoList = listOf(outdatedSearchedPhotoEntity, outdatedSearchedPhotoEntity)
        coEvery { searchLocalDataSource.search(null, any(), any()) } returns photoList
        coEvery { photoRemoteDataSource.getRecent(any(), any()) } returns searchResponse

        // whenever
        val actual = repository.searchPhotos("query", 0, 5)

        // then
        actual.test {
            awaitItem() shouldBeInstanceOf Either.Loading::class
            awaitItem() shouldBeEqualTo Either.Loading(
                emptyList<SearchedPhotoEntity>().toPagePhotos(5)
            )
            awaitItem() shouldBeInstanceOf Either.Success::class
            awaitComplete()
        }
        coVerify(exactly = 1) { searchLocalDataSource.search(null, any(), any()) }
        coVerify(exactly = 1) { photoRemoteDataSource.getRecent(any(), any()) }
        coVerify(exactly = 1) { searchLocalDataSource.saveSearch(any()) }
        confirmVerified(searchLocalDataSource)
        confirmVerified(searchLocalDataSource)
    }
//
//    @Test
//    fun `on getRecentPhotos gets data from remote if db data is empty`() = coroutineTestCase {
//        given {
//            whenever(
//                searchLocalDataSource.search(eq(null), any(), any())
//            ) doReturn emptyList()
//            whenever(
//                photoRemoteDataSource.getRecent(any(), any())
//            ) doReturn searchResponse
//        }
//        var result: Flow<Either<Page<Photo>>>? = null
//        whenever {
//            result = repository.getRecentPhotos(0, 5)
//        }
//        then {
//            val response = result?.last()
//            verify(searchLocalDataSource).search(eq(null), any(), any())
//            verify(photoRemoteDataSource).getRecent(any(), any())
//            verify(searchLocalDataSource).saveSearch(any())
//            assert(response is Either.Success)
//        }
//    }
//
//    @Test
//    fun `on getRecentPhotos gets data db again if remote throw exception`() = coroutineTestCase {
//        given {
//            whenever(
//                searchLocalDataSource.search(eq(null), any(), any())
//            ) doReturn emptyList()
//            whenever(
//                photoRemoteDataSource.getRecent(any(), any())
//            ) doThrow RuntimeException("")
//        }
//        var result: Flow<Either<Page<Photo>>>? = null
//        whenever {
//            result = repository.getRecentPhotos(0, 5)
//        }
//        then {
//            val response = result?.last()
//            verify(searchLocalDataSource, times(2)).search(eq(null), any(), any())
//            verify(photoRemoteDataSource).getRecent(any(), any())
//            assert(response is Either.Error)
//        }
//    }
//
//    @Test
//    fun `searchLocalPhotos calls local datasource`() = coroutineTestCase {
//        given {
//            whenever(
//                searchLocalDataSource.searchFlow(any(), any(), any(), any())
//            ) doReturn flowOf(listOf(searchedPhotoEntity, searchedPhotoEntity))
//        }
//        whenever {
//            repository.searchLocalPhotos("", 0, 10, 4).last()
//        }
//        then {
//            verify(searchLocalDataSource).searchFlow(any(), any(), any(), any())
//            verifyZeroInteractions(photoRemoteDataSource)
//        }
//    }
//
//    @Test
//    fun `on clearExpiredData clear data by local datasources`() = coroutineTestCase {
//        val expireTime = 10L
//        whenever {
//            repository.clearExpiredData(expireTime)
//        }
//        then {
//            verify(searchLocalDataSource, times(1)).clearExpiredQueries(expireTime)
//            verify(searchLocalDataSource, times(1)).clearExpiredQueries(expireTime)
//            verify(searchHistoryLocalDataSource, times(1)).clearExpiredQueries(expireTime)
//            verifyZeroInteractions(photoRemoteDataSource)
//            verifyNoMoreInteractions(searchLocalDataSource)
//            verifyNoMoreInteractions(searchHistoryLocalDataSource)
//        }
//    }
//
//    @Test
//    fun `on saveSearchQuery save query in search history local datasource`() = coroutineTestCase {
//        val query = "query"
//        whenever {
//            repository.saveSearchQuery(query)
//        }
//        then {
//            verify(searchHistoryLocalDataSource, times(1)).saveQuery(query)
//            verifyZeroInteractions(searchLocalDataSource)
//            verifyZeroInteractions(photoRemoteDataSource)
//            verifyNoMoreInteractions(searchHistoryLocalDataSource)
//        }
//    }
//
//    @Test
//    fun `on removeSuggestion remove query from search history local datasource`() =
//        coroutineTestCase {
//            val query = "query"
//            whenever {
//                repository.removeSuggestion(query)
//            }
//            then {
//                verify(searchHistoryLocalDataSource, times(1)).removeQuery(query)
//                verifyZeroInteractions(searchLocalDataSource)
//                verifyZeroInteractions(photoRemoteDataSource)
//                verifyNoMoreInteractions(searchHistoryLocalDataSource)
//            }
//        }
//
//    @Test
//    fun `on getSearchSuggestion get recent queries from search history local datasource`() =
//        coroutineTestCase {
//            val query = "query"
//            whenever {
//                repository.getSearchSuggestion(query, 10)
//            }
//            then {
//                verify(searchHistoryLocalDataSource, times(1)).getLatestQueries(query, 10)
//                verifyZeroInteractions(searchLocalDataSource)
//                verifyZeroInteractions(photoRemoteDataSource)
//                verifyNoMoreInteractions(searchHistoryLocalDataSource)
//            }
//        }
}