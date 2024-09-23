package com.rosahosseini.findr.platform.view

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.LocalPlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import com.rosahosseini.findr.library.imageloader.getAsyncImageLoader
import com.rosahosseini.findr.platform.navigation.FindrNaveHost
import com.rosahosseini.findr.ui.theme.FindrTheme

@OptIn(ExperimentalCoilApi::class)
@Composable
fun App(modifier: Modifier = Modifier) {
    FindrTheme {
        val context = LocalPlatformContext.current
        setSingletonImageLoaderFactory {
            getAsyncImageLoader(context)
        }

        val navController = rememberNavController()
        Scaffold(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) { paddingValues ->
            FindrNaveHost(
                navController = navController,
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(WindowInsets.systemBars)
                    .fillMaxSize()
            )
        }
    }
}
