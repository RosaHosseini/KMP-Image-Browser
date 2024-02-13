package com.rosahosseini.findr.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.rosahosseini.findr.library.ui.R

@Composable
fun Bookmark(
    isBookmarked: () -> Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val icon = if (isBookmarked()) R.drawable.ic_heart_filled else R.drawable.ic_heart_red
    Image(
        painter = painterResource(id = icon),
        contentDescription = stringResource(id = R.string.bookmark),
        modifier = modifier
            .fillMaxSize()
            .clickable { onClick() }
    )
}