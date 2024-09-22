package com.rosahosseini.findr.platform.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.rosahosseini.findr.feature.bookmark.navigation.bookmarkGraph
import com.rosahosseini.findr.feature.bookmark.navigation.navigateToBookmarks
import com.rosahosseini.findr.feature.photodetail.navigation.navigateToPhotoDetail
import com.rosahosseini.findr.feature.photodetail.navigation.photoDetailGraph
import com.rosahosseini.findr.feature.search.navigation.ROUTE_SEARCH
import com.rosahosseini.findr.feature.search.navigation.searchGraph

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun FindrNaveHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_SEARCH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        searchGraph(
            navigateToBookmarks = navController::navigateToBookmarks,
            navigateToPhotoDetail = navController::navigateToPhotoDetail
        )
        photoDetailGraph(
            navController = navController
        )
        bookmarkGraph(
            navController = navController,
            navigateToPhotoDetail = navController::navigateToPhotoDetail
        )
    }
}
