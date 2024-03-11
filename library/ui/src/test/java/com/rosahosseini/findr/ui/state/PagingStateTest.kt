package com.rosahosseini.findr.ui.state

import com.rosahosseini.findr.model.Page
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class PagingStateTest {

    private val currentState = PagingState(
        data = persistentListOf(1, 2),
        pageNumber = 10,
        status = PagingState.Status.Idle,
        exhausted = true,
        throwable = Throwable()
    )

    @Test
    fun `onSuccess if newPage_pageNumber is more than zero appends new items`() = runTest {
        // given
        val newPage = Page(
            items = listOf(3, 4),
            pageNumber = 1,
            pageSize = 2,
            hasNext = true
        )

        // whenever
        val nextState = currentState.onSuccess(newPage)

        // then
        nextState shouldBeEqualTo PagingState(
            pageNumber = 1,
            data = persistentListOf(1, 2, 3, 4),
            status = PagingState.Status.Success,
            exhausted = false,
            throwable = null
        )
    }


    @Test
    fun `onSuccess if newPage_pageNumber is zero replace new items`() = runTest {
        // given
        val newPage = Page(
            items = listOf(3, 4),
            pageNumber = 0,
            pageSize = 2,
            hasNext = true
        )

        // whenever
        val nextState = currentState.onSuccess(newPage)

        // then
        nextState shouldBeEqualTo PagingState(
            pageNumber = 0,
            data = persistentListOf(3, 4),
            status = PagingState.Status.Success,
            exhausted = false,
            throwable = null
        )
    }

    @Test
    fun `onSuccess if new page does not have next page returns an exhausted paging state`() =
        runTest {
            // given
            val newPage = Page(
                items = listOf(3, 4),
                pageNumber = 1,
                pageSize = 2,
                hasNext = false
            )

            // whenever
            val nextState = currentState.onSuccess(newPage)

            // then
            nextState shouldBeEqualTo PagingState(
                pageNumber = 1,
                data = persistentListOf(1, 2, 3, 4),
                status = PagingState.Status.Success,
                exhausted = true,
                throwable = null
            )
        }

    @Test
    fun `onFailure should returns a paging state with failure status`() = runTest {
        // given
        val failure = Throwable("test")

        // whenever
        val nextState = currentState.onFailure(failure)

        // then
        nextState shouldBeEqualTo currentState.copy(
            status = PagingState.Status.Failure,
            throwable = failure
        )
    }

    @Test
    fun `onLoading should returns a paging state with loading status and new page number`() =
        runTest {
            // whenever
            val nextState = currentState.onLoading(page = 2)

            // then
            nextState shouldBeEqualTo currentState.copy(
                pageNumber = 2,
                status = PagingState.Status.Loading,
                throwable = null
            )
        }

    @Test
    fun `if currentState is loading nextPage should be null`() = runTest {
        // given
        val currentState = currentState.copy(status = PagingState.Status.Loading, pageNumber = 10)

        // whenever
        val nextPage = currentState.nextPage

        // then
        nextPage shouldBe null
    }

    @Test
    fun `if currentState is refreshing nextPage should be null`() = runTest {
        // given
        val currentState =
            currentState.copy(status = PagingState.Status.Refreshing, pageNumber = 10)

        // whenever
        val nextPage = currentState.nextPage

        // then
        nextPage shouldBe null
    }


    @Test
    fun `if currentState is success and not exhausted nextPage should be one more than current page number`() =
        runTest {
            // given
            val currentState = currentState.copy(
                status = PagingState.Status.Success,
                pageNumber = 10,
                exhausted = false
            )

            // whenever
            val nextPage = currentState.nextPage

            // then
            nextPage shouldBeEqualTo 11
        }

    @Test
    fun `if currentState is success and exhausted nextPage should be null`() =
        runTest {
            // given
            val currentState = currentState.copy(
                status = PagingState.Status.Success,
                pageNumber = 10,
                exhausted = true
            )

            // whenever
            val nextPage = currentState.nextPage

            // then
            nextPage shouldBe null
        }

    @Test
    fun `if currentState is idle nextPage should be zero`() = runTest {
        // given
        val currentState = currentState.copy(
            status = PagingState.Status.Idle,
            pageNumber = 10
        )

        // whenever
        val nextPage = currentState.nextPage

        // then
        nextPage shouldBeEqualTo 0
    }

    @Test
    fun `if currentState is failure next page be same as current page number`() = runTest {
        // given
        val currentState = currentState.copy(status = PagingState.Status.Failure, pageNumber = 10)

        // whenever
        val nextPage = currentState.nextPage

        // then
        nextPage shouldBeEqualTo 10
    }
}