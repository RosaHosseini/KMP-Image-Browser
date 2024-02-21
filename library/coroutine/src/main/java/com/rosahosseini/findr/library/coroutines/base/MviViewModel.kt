package com.rosahosseini.findr.library.coroutines.base

import kotlinx.coroutines.flow.StateFlow

interface MviViewModel<Intent, State> {

    fun onIntent(intent: Intent)

    val state: StateFlow<State>
}
