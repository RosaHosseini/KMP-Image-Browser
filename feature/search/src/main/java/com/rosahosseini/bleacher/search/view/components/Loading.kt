package com.rosahosseini.bleacher.search.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosahosseini.bleacher.ui.theme.BleacherColor
import com.rosahosseini.bleacher.ui.widget.DotsPulsing

@Composable
fun BoxScope.LoadingScreen() {
    Box(modifier = Modifier.align(Alignment.Center)) {
        DotsPulsing(
            dotSize = 20.dp,
            delayUnit = 300,
            color = BleacherColor.BrandColor
        )
    }
}