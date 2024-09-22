package com.rosahosseini.findr.feature.photodetail.view

import androidx.compose.runtime.Composable
import com.rosahosseini.findr.feature.photodetail.navigation.PhotoDetailArgs

@Composable
internal fun PhotoDetailRoute(state: PhotoDetailArgs, onBackPressed: () -> Unit) {
    PhotoDetailScreen(state, onBackPressed)
}
