package com.rosahosseini.findr.platform.utils

import com.rosahosseini.findr.BuildKonfig
import com.rosahosseini.findr.domain.model.BuildConfiguration
import com.rosahosseini.findr.domain.model.Device
import com.rosahosseini.findr.platform.BuildConfig

internal actual fun configureBuildConfiguration(): BuildConfiguration {
    return BuildConfiguration(
        flickerApiKey = BuildKonfig.FLICKR_API_KEY,
        isDebug = BuildConfig.DEBUG,
        device = Device.ANDROID
    )
}
