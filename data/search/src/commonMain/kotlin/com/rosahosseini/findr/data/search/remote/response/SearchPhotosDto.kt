package com.rosahosseini.findr.data.search.remote.response

import com.rosahosseini.findr.data.common.remote.model.FlickrResponse
import com.rosahosseini.findr.data.common.remote.model.PhotoDto
import com.rosahosseini.findr.data.common.remote.model.toPhoto
import com.rosahosseini.findr.domain.model.Page
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SearchPhotosDto(
    @SerialName("photo") val photos: List<PhotoDto>?,
    @SerialName("page") val pageNumber: Int,
    @SerialName("pages") val endPage: Int,
    @SerialName("perpage") val pageSize: Int
) {
    companion object {
        val fake = SearchPhotosDto(
            photos = listOf(PhotoDto.fake, PhotoDto.fake),
            pageNumber = 1,
            endPage = 10,
            pageSize = 5
        )
    }
}

@Serializable
internal class SearchResponseDto(
    @SerialName("photos") override val data: SearchPhotosDto,
    override val stat: String,
    override val message: String?,
    override val code: Int?
) : FlickrResponse<SearchPhotosDto>

internal fun SearchPhotosDto.toPagePhotos() = Page(
    items = photos?.map { it.toPhoto() }.orEmpty(),
    pageNumber = pageNumber - 1,
    pageSize = pageSize,
    hasNext = pageNumber < endPage
)
