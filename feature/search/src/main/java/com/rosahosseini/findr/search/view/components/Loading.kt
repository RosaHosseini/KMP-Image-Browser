package com.rosahosseini.findr.search.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.ui.theme.FindrColor
import com.rosahosseini.findr.ui.widget.DotsPulsing

@Composable
fun BoxScope.LoadingScreen() {
    Box(modifier = Modifier.align(Alignment.Center)) {
        DotsPulsing(
            dotSize = 20.dp,
            delayUnit = 300,
            color = FindrColor.BrandColor
        )
    }
}