package com.rosahosseini.findr.remote.datasource

import com.rosahosseini.findr.model.BuildConfiguration
import com.rosahosseini.findr.remote.model.response.SearchResponseDto
import com.rosahosseini.findr.remote.service.PhotoService
import javax.inject.Inject

class PhotoRemoteDataSource @Inject constructor(
    private val service: PhotoService,
    private val buildConfiguration: BuildConfiguration
) {

    suspend fun search(text: String, limit: Int, page: Int): SearchResponseDto {
        val response = service.search(
            buildConfiguration.flickerApiKey,
            text,
            pageNumber = page + 1,
            limit
        )
        response.checkSuccess()
        return response
    }

    suspend fun getRecent(limit: Int, page: Int): SearchResponseDto {
        val response = service.getRecent(
            buildConfiguration.flickerApiKey,
            pageNumber = page + 1,
            limit
        )
        response.checkSuccess()
        return response
    }
}
