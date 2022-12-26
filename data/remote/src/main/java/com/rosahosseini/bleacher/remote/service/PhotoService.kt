package com.rosahosseini.bleacher.remote.service

import com.rosahosseini.bleacher.remote.model.response.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

internal const val URLS = "url_sq, url_t, url_s, url_q, url_m, url_n, url_z, url_c, url_l, url_o"

interface PhotoService {
    @GET("?method=flickr.photos.search&nojsoncallback=1&format=json")
    suspend fun search(
        @Query("api_key") apiKey: String,
        @Query("text") text: String? = null,
        @Query("page") pageNumber: Int = 1,
        @Query("per_page") limit: Int = 10,
        @Query("extras") extras: String = URLS
    ): SearchResponseDto

    @GET("?method=flickr.photos.getRecent&nojsoncallback=1&format=json")
    suspend fun getRecent(
        @Query("api_key") apiKey: String,
        @Query("page") pageNumber: Int = 1,
        @Query("per_page") limit: Int = 10,
        @Query("extras") extras: String = URLS
    ): SearchResponseDto
}