package com.rosahosseini.findr.library.coroutines.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

open class BaseViewModel : ViewModel() {
    fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> {
        return stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue
        )
    }

    companion object {
        private const val STOP_TIMEOUT_MILLIS = 5L
    }
}
