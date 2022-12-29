package com.rosahosseini.bleacher.repositoryimpl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.rosahosseini.bleacher.commontest.CoroutineTestRule
import com.rosahosseini.bleacher.commontest.coroutineTestCase
import com.rosahosseini.bleacher.local.datasource.SearchHistoryLocalDataSource
import com.rosahosseini.bleacher.local.datasource.SearchPhotoLocalDataSource
import com.rosahosseini.bleacher.model.Either
import com.rosahosseini.bleacher.model.Page
import com.rosahosseini.bleacher.model.Photo
import com.rosahosseini.bleacher.remote.datasource.PhotoRemoteDataSource
import com.rosahosseini.bleacher.repository.SearchRepository
import com.rosahosseini.bleacher.repositoryimpl.map.toPagePhotos
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import org.junit.Rule
import org.junit.Test

class SearchRepositoryTest {
    private val photoRemoteDataSource: PhotoRemoteDataSource = mock()
    private val searchLocalDataSource: SearchPhotoLocalDataSource = mock()
    private val searchHistoryLocalDataSource: SearchHistoryLocalDataSource = mock()

    private val repository: SearchRepository by lazy {
        SearchRepositoryImpl(
            photoRemoteDataSource,
            searchLocalDataSource,
            searchHistoryLocalDataSource
        )
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Test
    fun `on searchPhotos gets data from db if it isn't outdated`() = coroutineTestCase {
        val photoList = listOf(searchedPhotoEntity, searchedPhotoEntity)
        given {
            whenever(
                searchLocalDataSource.search(any(), any(), any())
            ) doReturn photoList
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.searchPhotos("query", 0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource).search(any(), any(), any())
            verifyZeroInteractions(photoRemoteDataSource)
            assert(response is Either.Success)
            assertEquals(response?.data, photoList.toPagePhotos(5))
        }
    }

    @Test
    fun `on searchPhotos gets data from remote if db data is outdated`() = coroutineTestCase {
        val photoList = listOf(outdatedSearchedPhotoEntity, outdatedSearchedPhotoEntity)
        given {
            whenever(
                searchLocalDataSource.search(any(), any(), any())
            ) doReturn photoList
            whenever(
                photoRemoteDataSource.search(any(), any(), any())
            ) doReturn searchResponse
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.searchPhotos("query", 0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource).search(any(), any(), any())
            verify(photoRemoteDataSource).search(any(), any(), any())
            assert(response is Either.Success)
        }
    }

    @Test
    fun `on searchPhotos gets data from remote if db data is empty`() = coroutineTestCase {
        given {
            whenever(
                searchLocalDataSource.search(any(), any(), any())
            ) doReturn emptyList()
            whenever(
                photoRemoteDataSource.search(any(), any(), any())
            ) doReturn searchResponse
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.searchPhotos("query", 0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource).search(any(), any(), any())
            verify(photoRemoteDataSource).search(any(), any(), any())
            assert(response is Either.Success)
        }
    }

    @Test
    fun `on searchPhotos gets data db again if remote throw exception`() = coroutineTestCase {
        given {
            whenever(
                searchLocalDataSource.search(any(), any(), any())
            ) doReturn emptyList()
            whenever(
                photoRemoteDataSource.search(any(), any(), any())
            ) doThrow RuntimeException("")
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.searchPhotos("query", 0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource, times(2)).search(any(), any(), any())
            verify(photoRemoteDataSource).search(any(), any(), any())
            assert(response is Either.Error)
        }
    }

    @Test
    fun `on getRecentPhotos gets data from db if it isn't outdated`() = coroutineTestCase {
        val photoList = listOf(searchedPhotoEntity, searchedPhotoEntity)
        given {
            whenever(
                searchLocalDataSource.search(eq(null), any(), any())
            ) doReturn photoList
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.getRecentPhotos(0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource).search(eq(null), any(), any())
            verifyZeroInteractions(photoRemoteDataSource)
            assert(response is Either.Success)
            assertEquals(response?.data, photoList.toPagePhotos(5))
        }
    }

    @Test
    fun `on getRecentPhotos gets data from remote if db data is outdated`() = coroutineTestCase {
        val photoList = listOf(outdatedSearchedPhotoEntity, outdatedSearchedPhotoEntity)
        given {
            whenever(
                searchLocalDataSource.search(eq(null), any(), any())
            ) doReturn photoList
            whenever(
                photoRemoteDataSource.getRecent(any(), any())
            ) doReturn searchResponse
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.getRecentPhotos(0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource).search(eq(null), any(), any())
            verify(photoRemoteDataSource).getRecent(any(), any())
            verify(searchLocalDataSource).saveSearch(any())
            assert(response is Either.Success)
        }
    }

    @Test
    fun `on getRecentPhotos gets data from remote if db data is empty`() = coroutineTestCase {
        given {
            whenever(
                searchLocalDataSource.search(eq(null), any(), any())
            ) doReturn emptyList()
            whenever(
                photoRemoteDataSource.getRecent(any(), any())
            ) doReturn searchResponse
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.getRecentPhotos(0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource).search(eq(null), any(), any())
            verify(photoRemoteDataSource).getRecent(any(), any())
            verify(searchLocalDataSource).saveSearch(any())
            assert(response is Either.Success)
        }
    }

    @Test
    fun `on getRecentPhotos gets data db again if remote throw exception`() = coroutineTestCase {
        given {
            whenever(
                searchLocalDataSource.search(eq(null), any(), any())
            ) doReturn emptyList()
            whenever(
                photoRemoteDataSource.getRecent(any(), any())
            ) doThrow RuntimeException("")
        }
        var result: Flow<Either<Page<Photo>>>? = null
        whenever {
            result = repository.getRecentPhotos(0, 5)
        }
        then {
            val response = result?.last()
            verify(searchLocalDataSource, times(2)).search(eq(null), any(), any())
            verify(photoRemoteDataSource).getRecent(any(), any())
            assert(response is Either.Error)
        }
    }

    @Test
    fun `searchLocalPhotos calls local datasource`() = coroutineTestCase {
        given {
            whenever(
                searchLocalDataSource.searchFlow(any(), any(), any(), any())
            ) doReturn flowOf(listOf(searchedPhotoEntity, searchedPhotoEntity))
        }
        whenever {
            repository.searchLocalPhotos("", 0, 10, 4).last()
        }
        then {
            verify(searchLocalDataSource).searchFlow(any(), any(), any(), any())
            verifyZeroInteractions(photoRemoteDataSource)
        }
    }

    @Test
    fun `on clearExpiredData clear data by local datasources`() = coroutineTestCase {
        val expireTime = 10L
        whenever {
            repository.clearExpiredData(expireTime)
        }
        then {
            verify(searchLocalDataSource, times(1)).clearExpiredQueries(expireTime)
            verify(searchLocalDataSource, times(1)).clearExpiredQueries(expireTime)
            verify(searchHistoryLocalDataSource, times(1)).clearExpiredQueries(expireTime)
            verifyZeroInteractions(photoRemoteDataSource)
            verifyNoMoreInteractions(searchLocalDataSource)
            verifyNoMoreInteractions(searchHistoryLocalDataSource)
        }
    }

    @Test
    fun `on saveSearchQuery save query in search history local datasource`() = coroutineTestCase {
        val query = "query"
        whenever {
            repository.saveSearchQuery(query)
        }
        then {
            verify(searchHistoryLocalDataSource, times(1)).saveQuery(query)
            verifyZeroInteractions(searchLocalDataSource)
            verifyZeroInteractions(photoRemoteDataSource)
            verifyNoMoreInteractions(searchHistoryLocalDataSource)
        }
    }

    @Test
    fun `on removeSuggestion remove query from search history local datasource`() =
        coroutineTestCase {
            val query = "query"
            whenever {
                repository.removeSuggestion(query)
            }
            then {
                verify(searchHistoryLocalDataSource, times(1)).removeQuery(query)
                verifyZeroInteractions(searchLocalDataSource)
                verifyZeroInteractions(photoRemoteDataSource)
                verifyNoMoreInteractions(searchHistoryLocalDataSource)
            }
        }

    @Test
    fun `on getSearchSuggestion get recent queries from search history local datasource`() =
        coroutineTestCase {
            val query = "query"
            whenever {
                repository.getSearchSuggestion(query, 10)
            }
            then {
                verify(searchHistoryLocalDataSource, times(1)).getLatestQueries(query, 10)
                verifyZeroInteractions(searchLocalDataSource)
                verifyZeroInteractions(photoRemoteDataSource)
                verifyNoMoreInteractions(searchHistoryLocalDataSource)
            }
        }
}