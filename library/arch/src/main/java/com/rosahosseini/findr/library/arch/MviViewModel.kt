package com.rosahosseini.findr.library.arch

import kotlinx.coroutines.flow.StateFlow

interface MviViewModel<Intent, State> {

    fun onIntent(intent: Intent)

    val state: StateFlow<State>
}
