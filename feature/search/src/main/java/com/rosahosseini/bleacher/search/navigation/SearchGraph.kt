package com.rosahosseini.bleacher.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rosahosseini.bleacher.navigation.destinations.SearchDestination
import com.rosahosseini.bleacher.search.view.SearchRoot

fun NavGraphBuilder.searchGraph() {
    composable(route = SearchDestination.route) {
        SearchRoot()
    }
}