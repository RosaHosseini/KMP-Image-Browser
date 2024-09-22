package com.rosahosseini.findr.feature.photodetail.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rosahosseini.findr.feature.photodetail.view.PhotoDetailRoute

private const val BASE_PHOTO_DETAIL = "photo_detail_route"
internal const val ARG_URL = "url"
internal const val ARG_DESCRIPTION = "photo_id"
internal const val ARG_TITLE = "title"
private const val ROUTE_PHOTO_DETAIL =
    "$BASE_PHOTO_DETAIL?url={$ARG_URL}&title={$ARG_TITLE}&desc={$ARG_DESCRIPTION}"

fun NavGraphBuilder.photoDetailGraph(navController: NavController) {
    composable(
        route = ROUTE_PHOTO_DETAIL,
        arguments = listOf(
            navArgument(ARG_TITLE) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(ARG_URL) {
                type = NavType.StringType
            },
            navArgument(ARG_DESCRIPTION) {
                type = NavType.StringType
                nullable = true
            }
        )
    ) {
        PhotoDetailRoute(
            state = requireNotNull(it.photoDetailArgs()),
            onBackPressed = navController::navigateUp
        )
    }
}

private fun NavBackStackEntry.photoDetailArgs(): PhotoDetailArgs? {
    arguments ?: return null
    return PhotoDetailArgs(
        url = arguments?.getString(ARG_URL) ?: return null,
        title = arguments?.getString(ARG_TITLE),
        description = arguments?.getString(ARG_DESCRIPTION)
    )
}

fun NavController.navigateToPhotoDetail(
    url: String,
    title: String?,
    description: String?,
    navOptions: NavOptions? = null
) {
    val route = buildString {
        append("$BASE_PHOTO_DETAIL?url=$url")
        title?.let { append("&title=$title") }
        description?.let { append("&desc=$description") }
    }
    navigate(route, navOptions)
}
