package com.rosahosseini.findr.ui.extensions

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@SuppressLint("ComposableNaming")
@Composable
fun <T : Any?> T.doOncePerInstance(block: (T) -> Unit) {
    var oldHashCode by rememberSaveable { mutableIntStateOf(0) }
    val hashCode = hashCode()
    if (oldHashCode != hashCode) {
        SideEffect {
            block(this@doOncePerInstance)
            oldHashCode = hashCode
        }
    }
}
