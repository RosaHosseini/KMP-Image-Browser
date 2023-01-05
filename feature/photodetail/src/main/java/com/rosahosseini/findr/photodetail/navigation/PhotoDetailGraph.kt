package com.rosahosseini.findr.photodetail.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.navigation.destinations.PhotoDetailDestination
import com.rosahosseini.findr.navigation.extensions.getArg
import com.rosahosseini.findr.photodetail.view.PhotoDetailRoute

fun NavGraphBuilder.photoDetailGraph(navController: NavController) {
    composable(route = PhotoDetailDestination.route) {
        val photo: Photo? by rememberSaveable {
            mutableStateOf(navController.getArg(PhotoDetailDestination.arg))
        }
        PhotoDetailRoute(requireNotNull(photo), onBackPressed = navController::popBackStack)
    }
}