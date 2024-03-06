package com.rosahosseini.findr.data.search.remote.service

import com.rosahosseini.findr.data.search.remote.response.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

private const val URLS = "url_sq, url_t, url_s, url_q, url_m, url_n, url_z, url_c, url_l, url_o"

internal interface SearchService {
    @GET("?method=flickr.photos.search&nojsoncallback=1&format=json")
    suspend fun search(
        @Query("api_key") apiKey: String,
        @Query("text") text: String? = null,
        @Query("page") pageNumber: Int = 1,
        @Query("per_page") limit: Int = 10,
        @Query("extras") extras: String = URLS
    ): Result<SearchResponseDto>

    @GET("?method=flickr.photos.getRecent&nojsoncallback=1&format=json")
    suspend fun getRecent(
        @Query("api_key") apiKey: String,
        @Query("page") pageNumber: Int = 1,
        @Query("per_page") limit: Int = 10,
        @Query("extras") extras: String = URLS
    ): Result<SearchResponseDto>
}
