package com.rosahosseini.findr.bookmark.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rosahosseini.findr.bookmark.view.BookmarkRoute
import com.rosahosseini.findr.navigation.destinations.BookmarkDestination

fun NavGraphBuilder.bookmarkGraph() {
    composable(route = BookmarkDestination.route) {
        BookmarkRoute()
    }
}