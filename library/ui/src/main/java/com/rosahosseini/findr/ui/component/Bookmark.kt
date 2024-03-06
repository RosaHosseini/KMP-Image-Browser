package com.rosahosseini.findr.ui.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rosahosseini.findr.library.ui.R
import com.rosahosseini.findr.ui.theme.FindrTheme

@Composable
fun Bookmark(
    enable: Boolean,
    modifier: Modifier = Modifier
) {
    val icon = if (enable) R.drawable.ic_heart_filled else R.drawable.ic_heart_red
    Image(
        painter = painterResource(id = icon),
        contentDescription = stringResource(id = R.string.bookmark),
        modifier = modifier
    )
}

@Preview
@Composable
private fun EnabledBookmarkPreview() {
    FindrTheme {
        Bookmark(enable = true)
    }
}

@Preview
@Composable
private fun DisabledBookmarkPreview() {
    FindrTheme {
        Bookmark(enable = false)
    }
}
