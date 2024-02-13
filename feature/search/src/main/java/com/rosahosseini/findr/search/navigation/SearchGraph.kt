package com.rosahosseini.findr.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.search.view.SearchRoute


const val ROUTE_SEARCH = "search_root"

fun NavGraphBuilder.searchGraph(
    navigateToPhotoDetail: (
        url: String,
        title: String?,
        description: String?
    ) -> Unit,
    navigateToBookmarks: () -> Unit,
) {
    composable(route = ROUTE_SEARCH) {
        SearchRoute(navigateToPhotoDetail, navigateToBookmarks)
    }
}

fun NavController.navigateToSearch(navOptions: NavOptions?) {
    navigate(ROUTE_SEARCH, navOptions)
}