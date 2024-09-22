package com.rosahosseini.findr.platform.di

import com.rosahosseini.findr.domain.model.BuildConfiguration
import com.rosahosseini.findr.platform.utils.configureBuildConfiguration
import org.koin.dsl.module

internal val appModule = module {
    factory<BuildConfiguration> { configureBuildConfiguration() }
}
