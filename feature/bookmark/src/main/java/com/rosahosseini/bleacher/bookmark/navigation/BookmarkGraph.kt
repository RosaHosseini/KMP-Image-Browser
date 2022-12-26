package com.rosahosseini.bleacher.bookmark.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rosahosseini.bleacher.bookmark.view.BookmarkRoute
import com.rosahosseini.bleacher.navigation.destinations.BookmarkDestination

fun NavGraphBuilder.bookmarkGraph() {
    composable(route = BookmarkDestination.route) {
        BookmarkRoute()
    }
}