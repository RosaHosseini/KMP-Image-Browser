package com.rosahosseini.findr.remote.model.response

import com.google.gson.annotations.SerializedName

data class SearchPhotosDto(
    @SerializedName("photo") val photos: List<PhotoDto>?,
    @SerializedName("page") val pageNumber: Int,
    @SerializedName("pages") val endPage: Int,
    @SerializedName("perpage") val pageSize: Int
)

class SearchResponseDto(
    @SerializedName("photos") val data: SearchPhotosDto,
    status: String,
    errorManage: String?,
    errorCode: Int?
) : FlickrResponse(status, errorManage, errorCode)