package com.rosahosseini.findr.photodetail.view

import androidx.compose.runtime.Composable
import com.rosahosseini.findr.photodetail.navigation.PhotoDetailArgs

@Composable
internal fun PhotoDetailRoute(state: PhotoDetailArgs, onBackPressed: () -> Unit) {
    PhotoDetailScreen(state, onBackPressed)
}
