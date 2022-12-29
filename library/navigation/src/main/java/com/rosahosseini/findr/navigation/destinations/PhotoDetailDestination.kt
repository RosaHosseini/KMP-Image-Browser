package com.rosahosseini.findr.navigation.destinations

import com.rosahosseini.findr.navigation.NavigationDestination

object PhotoDetailDestination : NavigationDestination {
    override val route = "photo_detail_route"
    const val arg = "photo"
}