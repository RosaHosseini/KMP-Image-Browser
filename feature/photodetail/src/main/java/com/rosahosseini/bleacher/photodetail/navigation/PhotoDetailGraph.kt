package com.rosahosseini.bleacher.photodetail.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rosahosseini.bleacher.model.Photo
import com.rosahosseini.bleacher.navigation.destinations.PhotoDetailDestination
import com.rosahosseini.bleacher.navigation.extensions.getArg
import com.rosahosseini.bleacher.photodetail.view.PhotoDetailRoute

fun NavGraphBuilder.photoDetailGraph(navController: NavController) {
    composable(route = PhotoDetailDestination.route) {
        val photo: Photo? by rememberSaveable {
            mutableStateOf(navController.getArg(PhotoDetailDestination.arg))
        }
        PhotoDetailRoute({ requireNotNull(photo) }) { navController.popBackStack() }
    }
}