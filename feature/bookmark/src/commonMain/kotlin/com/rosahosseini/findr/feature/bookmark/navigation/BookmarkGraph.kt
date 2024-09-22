package com.rosahosseini.findr.feature.bookmark.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rosahosseini.findr.feature.bookmark.view.BookmarkRoute

private const val ROUTE_BOOKMARK = "bookmarks_route"

fun NavGraphBuilder.bookmarkGraph(
    navController: NavController,
    navigateToPhotoDetail: (
        url: String,
        title: String?,
        description: String?
    ) -> Unit
) {
    composable(route = ROUTE_BOOKMARK) {
        BookmarkRoute(
            navigateToPhotoDetail = navigateToPhotoDetail,
            onBackPressed = navController::navigateUp
        )
    }
}

fun NavController.navigateToBookmarks(navOptions: NavOptions? = null) {
    navigate(ROUTE_BOOKMARK, navOptions)
}
