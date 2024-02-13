package com.rosahosseini.findr.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.rosahosseini.findr.app.navigation.FindrNaveHost
import com.rosahosseini.findr.ui.theme.FindrColor
import com.rosahosseini.findr.ui.theme.FindrTheme

@Composable
fun App(navigator: Navigator) {
    ProvideWindowInsets {
        FindrTheme {
            val navController = rememberNavController()
            Scaffold(
                modifier = Modifier.padding(),
                containerColor = FindrColor.GreyBackground,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ) { padding ->
                FindrNaveHost(
                    navController = navController,
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    navigator = navigator
                )
            }
        }
    }
}