package com.rosahosseini.findr.platform.utils

import com.rosahosseini.findr.BuildKonfig
import com.rosahosseini.findr.domain.model.BuildConfiguration
import com.rosahosseini.findr.domain.model.Device
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

@OptIn(ExperimentalNativeApi::class)
internal actual fun configureBuildConfiguration(): BuildConfiguration {
    return BuildConfiguration(
        isDebug = Platform.isDebugBinary,
        flickerApiKey = BuildKonfig.FLICKR_API_KEY,
        device = Device.IOS
    )
}
