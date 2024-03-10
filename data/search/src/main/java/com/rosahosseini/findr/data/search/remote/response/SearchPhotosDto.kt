package com.rosahosseini.findr.data.search.remote.response

import com.google.gson.annotations.SerializedName
import com.rosahosseini.findr.flicker.FlickrResponse
import com.rosahosseini.findr.model.Page
import com.rosahosseini.findr.remote.model.PhotoDto
import com.rosahosseini.findr.remote.model.toPhoto

internal data class SearchPhotosDto(
    @SerializedName("photo") val photos: List<PhotoDto>?,
    @SerializedName("page") val pageNumber: Int,
    @SerializedName("pages") val endPage: Int,
    @SerializedName("perpage") val pageSize: Int
)

internal class SearchResponseDto(
    @SerializedName("photos") override val data: SearchPhotosDto,
    status: String,
    errorMassage: String?,
    errorCode: Int?
) : FlickrResponse<SearchPhotosDto>(data, status, errorMassage, errorCode)

internal fun SearchPhotosDto.toPagePhotos() = Page(
    items = photos?.map { it.toPhoto() }.orEmpty(),
    pageNumber = pageNumber - 1,
    pageSize = pageSize,
    hasNext = pageNumber < endPage
)
