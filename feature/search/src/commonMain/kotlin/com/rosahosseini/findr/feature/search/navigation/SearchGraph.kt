package com.rosahosseini.findr.feature.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rosahosseini.findr.feature.search.view.SearchRoute

const val ROUTE_SEARCH = "search_root"

fun NavGraphBuilder.searchGraph(
    navigateToPhotoDetail: (
        url: String,
        title: String?,
        description: String?
    ) -> Unit,
    navigateToBookmarks: () -> Unit
) {
    composable(route = ROUTE_SEARCH) {
        SearchRoute(navigateToPhotoDetail, navigateToBookmarks)
    }
}
