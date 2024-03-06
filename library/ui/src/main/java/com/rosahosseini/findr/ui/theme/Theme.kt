package com.rosahosseini.findr.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.rosahosseini.findr.ui.theme.colors.palletDark

@Composable
fun FindrTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = Typography,
        shapes = Shapes,
        content = content,
        colorScheme = palletDark
    )
}
