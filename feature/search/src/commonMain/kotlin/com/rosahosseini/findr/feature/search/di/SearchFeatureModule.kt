package com.rosahosseini.findr.feature.search.di

import com.rosahosseini.findr.feature.search.viewmodel.SearchViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchViewmodelModule: Module = module {
    viewModelOf(::SearchViewModel)
}
