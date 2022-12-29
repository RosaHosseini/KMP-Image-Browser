package com.rosahosseini.findr.navigation.extensions

import android.os.Parcelable
import androidx.navigation.NavController
import com.rosahosseini.findr.navigation.NavigationAction

fun <T> NavController.getArg(key: String): T? =
    previousBackStackEntry?.savedStateHandle?.get<T>(key)

fun NavController.saveArg(key: String, value: Parcelable) {
    currentBackStackEntry?.savedStateHandle?.apply {
        set(key, value)
    }
}

fun NavController.navigate(navigationAction: NavigationAction) {
    when (navigationAction) {
        is NavigationAction.PopBackStack -> popBackStack()
        is NavigationAction.NavigateDestination -> {
            if (currentDestination?.route != navigationAction.route) {
                // save args
                navigationAction.args.forEach { pair -> saveArg(pair.first, pair.second) }
                navigate(navigationAction.route)
            }
        }
    }
}