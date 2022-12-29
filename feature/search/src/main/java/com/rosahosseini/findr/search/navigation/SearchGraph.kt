package com.rosahosseini.findr.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rosahosseini.findr.navigation.destinations.SearchDestination
import com.rosahosseini.findr.search.view.SearchRoot

fun NavGraphBuilder.searchGraph() {
    composable(route = SearchDestination.route) {
        SearchRoot()
    }
}