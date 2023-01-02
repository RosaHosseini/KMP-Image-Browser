package com.rosahosseini.findr.repository

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rosahosseini.findr.commontest.CoroutineTestRule
import com.rosahosseini.findr.commontest.coroutineTestCase
import com.rosahosseini.findr.local.datasource.BookmarkLocalDataSource
import com.rosahosseini.findr.model.Either
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.repository.impl.BookmarkRepositoryImpl
import com.rosahosseini.findr.repository.map.toPhoto
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class BookmarkRepositoryTest {
    private val bookmarkLocalDataSource: BookmarkLocalDataSource = mock()
    private val repository: BookmarkRepository by lazy {
        BookmarkRepositoryImpl(bookmarkLocalDataSource)
    }

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Test
    fun `on changeBookmarkState change bookmark state in db`() = coroutineTestCase {
        val photoId = ""
        val isBookmark = false
        whenever {
            repository.changeBookmarkState(photoId, isBookmark)
        }
        then {
            verify(bookmarkLocalDataSource).changeBookmarkState(photoId, isBookmark)
        }
    }

    @Test
    fun `on changeBookmarkState return EitherError if exception happen`() = coroutineTestCase {
        given {
            whenever(
                bookmarkLocalDataSource.changeBookmarkState(
                    any(),
                    any()
                )
            ) doThrow RuntimeException("")
        }
        var result: Either<Unit>? = null
        whenever {
            result = repository.changeBookmarkState("", false)
        }
        then {
            assert(result is Either.Error)
        }
    }

    @Test
    fun `on changeBookmarkState return EitherSuccess if exception not happens`() =
        coroutineTestCase {
            var result: Either<Unit>? = null
            whenever {
                result = repository.changeBookmarkState("", false)
            }
            then {
                assert(result is Either.Success)
            }
        }

    @Test
    fun ` getAllBookmarkedPhotos would call local datasource`() = coroutineTestCase {
        val photos = listOf(photo, photo)
        given {
            whenever(
                bookmarkLocalDataSource.getBookmarkedPhotos()
            ) doReturn flowOf(photos)
        }
        var result: Flow<List<Photo>>? = null
        whenever {
            result = repository.getAllBookmarkedPhotos()
        }
        then {
            verify(bookmarkLocalDataSource).getBookmarkedPhotos()
            assertEquals(result?.first(), photos.map { it.toPhoto() })
        }
    }
}