package com.rosahosseini.findr.domain.model

data class BuildConfiguration(
    val flickerApiKey: String,
    val isDebug: Boolean,
    val device: Device
)

enum class Device {
    ANDROID,
    IOS
}
