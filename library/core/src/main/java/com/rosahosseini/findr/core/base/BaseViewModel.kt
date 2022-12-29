package com.rosahosseini.findr.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosahosseini.findr.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
open class BaseViewModel @Inject constructor(private val navigator: Navigator) : ViewModel() {
    fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> {
        return stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue
        )
    }

    fun onBackPressed() {
        viewModelScope.launch {
            navigator.popBackStack()
        }
    }

    companion object {
        private const val STOP_TIMEOUT_MILLIS = 5L
    }
}