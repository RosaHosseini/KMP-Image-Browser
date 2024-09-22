package com.rosahosseini.findr.data.common.remote.model

import com.rosahosseini.findr.data.common.extensions.orNullIfEmpty
import com.rosahosseini.findr.domain.model.Photo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String?,
    @SerialName("description") val description: String?,
    @SerialName("url_o") val urlOriginal: String?,
    @SerialName("url_l") val urlLarge: String?,
    @SerialName("url_c") val urlMedium800px: String?,
    @SerialName("url_z") val urlMedium640px: String?,
    @SerialName("url_n") val urlSmall320px: String?,
    @SerialName("url_m") val urlSmall240px: String?,
    @SerialName("url_q") val urlThumbnail150px: String?,
    @SerialName("url_t") val urlThumbnail100px: String?,
    @SerialName("url_s") val urlThumbnail75px: String?,
    @SerialName("url_sq") val urlThumbnailSquare: String?
) {
    companion object {
        val fake = PhotoDto(
            id = "",
            title = null,
            description = null,
            urlOriginal = "",
            urlThumbnail150px = null,
            urlThumbnail100px = null,
            urlThumbnail75px = null,
            urlThumbnailSquare = null,
            urlLarge = null,
            urlMedium640px = null,
            urlMedium800px = null,
            urlSmall240px = null,
            urlSmall320px = null
        )
    }
}

fun PhotoDto.toPhoto() =
    Photo(
        id = id,
        title = title,
        description = description,
        url = urlOriginal?.orNullIfEmpty()
            ?: urlLarge?.orNullIfEmpty()
            ?: urlMedium800px?.orNullIfEmpty()
            ?: urlMedium640px?.orNullIfEmpty()
            ?: urlSmall320px?.orNullIfEmpty()
            ?: urlSmall240px?.orNullIfEmpty()
            ?: "",
        thumbnailUrl = urlThumbnail150px?.orNullIfEmpty()
            ?: urlThumbnail100px?.orNullIfEmpty()
            ?: urlThumbnail75px?.orNullIfEmpty()
            ?: urlThumbnailSquare?.orNullIfEmpty()
    )
