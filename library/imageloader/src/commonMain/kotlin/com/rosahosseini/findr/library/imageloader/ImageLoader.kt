package com.rosahosseini.findr.library.imageloader

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.SubcomposeAsyncImage
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.CacheStrategy
import coil3.network.NetworkFetcher
import coil3.network.ktor.asNetworkClient
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import okio.FileSystem
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoadImage(
    url: String,
    description: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    SubcomposeAsyncImage(
        model = url,
        error = { painterResource(Res.drawable.ic_error) },
        loading = { painterResource(Res.drawable.ic_placeholder) },
        contentDescription = description,
        contentScale = contentScale,
        modifier = modifier
    )
}

fun getAsyncImageLoader(
    context: PlatformContext
): ImageLoader {
    return ImageLoader.Builder(context)
        .components { add(ktorNetworkFetcherFactory()) }
        .memoryCachePolicy(CachePolicy.ENABLED)
        .memoryCache { memoryCache(context) }
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .diskCache { diskCache() }
        .crossfade(true)
        .logger(DebugLogger())
        .build()
}

internal expect fun getNetworkClientEngine(): HttpClientEngine

@OptIn(ExperimentalCoilApi::class)
private fun ktorNetworkFetcherFactory() = NetworkFetcher.Factory(
    networkClient = { HttpClient(getNetworkClientEngine()).asNetworkClient() },
    cacheStrategy = { CacheStrategy() }
)

private fun memoryCache(context: PlatformContext): MemoryCache {
    return MemoryCache.Builder()
        .maxSizePercent(context, 0.3)
        .weakReferencesEnabled(true)
        .build()
}

private fun diskCache(): DiskCache {
    return DiskCache.Builder()
        .directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
        .maxSizeBytes(512L * 1024 * 1024) // 512MB
        .build()
}
