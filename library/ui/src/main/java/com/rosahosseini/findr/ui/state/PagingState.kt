package com.rosahosseini.findr.ui.state

import androidx.compose.runtime.Immutable
import com.rosahosseini.findr.model.Page
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class PagingState<T>(
    private val pageNumber: Int = 0,
    val data: ImmutableList<T> = persistentListOf(),
    val status: Status = Status.Idle,
    val exhausted: Boolean = false,
    val throwable: Throwable? = null
) {
    enum class Status {
        Idle,
        Loading,
        Refreshing,
        Success,
        Failure
    }

    val isAppending: Boolean get() = pageNumber > 0

    val canRefresh: Boolean get() = status != Status.Loading

    val refreshing: Boolean get() = status == Status.Refreshing

    val nextPage: Int?
        get() {
            return when (status) {
                Status.Idle -> 0
                Status.Failure -> pageNumber
                Status.Success -> if (exhausted) null else pageNumber + 1
                Status.Loading, Status.Refreshing -> null
            }
        }
}

fun <T> PagingState<T>.onSuccess(newPage: Page<T>): PagingState<T> {
    val items = if (newPage.pageNumber == 0) newPage.items else (data + newPage.items)
    return copy(
        status = PagingState.Status.Success,
        data = items.toImmutableList(),
        pageNumber = newPage.pageNumber,
        exhausted = !newPage.hasNext,
        throwable = null
    )
}

fun <T> PagingState<T>.onLoading(page: Int): PagingState<T> {
    return copy(
        status = PagingState.Status.Loading,
        data = if (page == 0) persistentListOf() else data,
        pageNumber = page,
        throwable = null
    )
}

fun <T> PagingState<T>.onRefresh(): PagingState<T> {
    return copy(
        status = PagingState.Status.Refreshing
    )
}

fun <T> PagingState<T>.onFailure(throwable: Throwable?): PagingState<T> {
    return copy(
        status = PagingState.Status.Failure,
        throwable = throwable
    )
}
