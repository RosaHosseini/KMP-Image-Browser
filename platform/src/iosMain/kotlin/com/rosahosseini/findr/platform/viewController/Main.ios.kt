package com.rosahosseini.findr.platform.viewController

import androidx.compose.ui.window.ComposeUIViewController
import com.rosahosseini.findr.platform.di.initKoin
import com.rosahosseini.findr.platform.view.App
import platform.UIKit.UIViewController

@Suppress("FunctionName", "unused")
fun MainViewController(): UIViewController =
    ComposeUIViewController(
        configure = {
            enforceStrictPlistSanityCheck = false
            initKoin()
        },
        content = { App() }
    )
