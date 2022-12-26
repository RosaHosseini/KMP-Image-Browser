package com.rosahosseini.bleacher.repositoryimpl

import org.junit.jupiter.api.Assertions.*

class BookmarkRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Test
    fun `onConfirmTransaction would dispatch Confirm in transactionManager`() = coroutineTestCase {
        val isConfirmed = true
        whenever {
            viewModel.onConfirmTransaction(isConfirmed)
        }
        then {
            verify(transactionManager).dispatch(Input.Confirm(isConfirmed))
        }
    }
}