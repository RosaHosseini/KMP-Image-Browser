package com.rosahosseini.findr.data.search.remote.datasource

import com.rosahosseini.findr.data.search.remote.response.SearchPhotosDto
import com.rosahosseini.findr.data.search.remote.service.SearchService
import com.rosahosseini.findr.model.BuildConfiguration
import javax.inject.Inject

internal class PhotoRemoteDataSource @Inject constructor(
    private val service: SearchService,
    buildConfiguration: BuildConfiguration
) {

    private val apiKey = buildConfiguration.flickerApiKey

    suspend fun search(text: String, limit: Int, page: Int): Result<SearchPhotosDto> {
        return service
            .search(apiKey = apiKey, text = text, pageNumber = page + 1, limit = limit)
            .toResult()
    }

    suspend fun getRecent(limit: Int, page: Int): Result<SearchPhotosDto> {
        return service
            .getRecent(apiKey = apiKey, pageNumber = page + 1, limit = limit)
            .toResult()
    }
}
