package com.rosahosseini.findr.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "light",
    showSystemUi = true,
    backgroundColor = 0xFFFAFAFA,
    device = "id:pixel_6",
    showBackground = true,
    heightDp = 700
)
@Preview(
    name = "dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
    backgroundColor = 0xFF212121,
    device = "id:pixel_6",
    showBackground = true,
    heightDp = 700
)
annotation class ScreenPreviews
