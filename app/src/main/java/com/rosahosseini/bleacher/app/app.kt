package com.rosahosseini.bleacher.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.rosahosseini.bleacher.app.navigation.BleacherNaveHost
import com.rosahosseini.bleacher.navigation.Navigator
import com.rosahosseini.bleacher.ui.theme.BleacherColor
import com.rosahosseini.bleacher.ui.theme.BleacherTheme

@Composable
fun App(navigator: Navigator) {
    ProvideWindowInsets {
        BleacherTheme {
            val navController = rememberNavController()
            Scaffold(
                modifier = Modifier.padding(),
                backgroundColor = BleacherColor.GreyBackground,
                contentColor = MaterialTheme.colors.onBackground,
            ) { padding ->
                BleacherNaveHost(
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