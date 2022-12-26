package com.rosahosseini.bleacher.navigation

import android.os.Parcelable

sealed class NavigationAction {

    data class NavigateDestination(
        val route: String,
        val args: List<Pair<String, Parcelable>> = emptyList()
    ) : NavigationAction()

    object PopBackStack : NavigationAction()
}