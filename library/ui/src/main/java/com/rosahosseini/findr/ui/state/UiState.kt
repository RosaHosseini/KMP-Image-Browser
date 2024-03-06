package com.rosahosseini.findr.ui.state

sealed interface UiState<T> {

    val data: T?
    data class Idle<T>(override val data: T?) : UiState<T>
    data class Loading<T>(override val data: T?) : UiState<T>
    data class Success<T>(override val data: T) : UiState<T>
    data class Failure<T>(override val data: T?, val throwable: Throwable) : UiState<T>
}
