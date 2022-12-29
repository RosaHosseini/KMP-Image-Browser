package com.rosahosseini.findr.remote.datasource

import com.rosahosseini.findr.remote.model.response.SearchResponseDto
import com.rosahosseini.findr.remote.retrofit.API_KEY
import com.rosahosseini.findr.remote.service.PhotoService
import javax.inject.Inject

class PhotoRemoteDataSource @Inject constructor(private val service: PhotoService) {

    suspend fun search(text: String, limit: Int, page: Int): SearchResponseDto {
        val response = service.search(API_KEY, text, page + 1, limit)
        response.checkSuccess()
        return response
    }

    suspend fun getRecent(limit: Int, page: Int): SearchResponseDto {
        val response = service.getRecent(API_KEY, page + 1, limit)
        response.checkSuccess()
        return response
    }
}