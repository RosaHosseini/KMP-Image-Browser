package com.rosahosseini.findr.ui.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rosahosseini.findr.library.ui.Res
import com.rosahosseini.findr.library.ui.bookmark
import com.rosahosseini.findr.library.ui.ic_heart
import com.rosahosseini.findr.library.ui.ic_heart_filled
import com.rosahosseini.findr.ui.theme.FindrTheme
import com.rosahosseini.findr.ui.theme.colors.FindrColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Bookmark(
    enable: Boolean,
    modifier: Modifier = Modifier
) {
    val icon = if (enable) Res.drawable.ic_heart_filled else Res.drawable.ic_heart
    Icon(
        painter = painterResource(icon),
        tint = FindrColor.Red00,
        contentDescription = stringResource(Res.string.bookmark),
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
