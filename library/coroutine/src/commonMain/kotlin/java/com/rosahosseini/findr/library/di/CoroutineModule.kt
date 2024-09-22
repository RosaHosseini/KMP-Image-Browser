package java.com.rosahosseini.findr.library.di

import java.com.rosahosseini.findr.library.coroutines.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val coroutineModule = module {
    factory {
        CoroutineDispatchers(
            Dispatchers.Main,
            Dispatchers.Default,
            Dispatchers.IO
        )
    }
}
