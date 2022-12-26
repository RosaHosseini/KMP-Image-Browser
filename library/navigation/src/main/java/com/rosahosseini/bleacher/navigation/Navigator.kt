package com.rosahosseini.bleacher.navigation

import android.os.Parcelable
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Singleton
class Navigator @Inject constructor() {

    private val _navigationActionFlow = MutableSharedFlow<NavigationAction?>(1)
    val navigationActionFlow: SharedFlow<NavigationAction?> = _navigationActionFlow

    suspend fun navigateTo(
        navTarget: NavigationDestination,
        args: List<Pair<String, Parcelable>> = emptyList()
    ) {
        _navigationActionFlow.emit(NavigationAction.NavigateDestination(navTarget.route, args))
    }

    suspend fun popBackStack() {
        _navigationActionFlow.emit(NavigationAction.PopBackStack)
    }

    companion object {
        const val KEY = "navigation"
    }
}