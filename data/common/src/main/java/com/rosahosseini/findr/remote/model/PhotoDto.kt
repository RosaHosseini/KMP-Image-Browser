package com.rosahosseini.findr.remote.model

import com.google.gson.annotations.SerializedName
import com.rosahosseini.findr.extensions.orNullIfEmpty
import com.rosahosseini.findr.model.Photo

data class PhotoDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("url_o") val urlOriginal: String?,
    @SerializedName("url_l") val urlLarge: String?,
    @SerializedName("url_c") val urlMedium800px: String?,
    @SerializedName("url_z") val urlMedium640px: String?,
    @SerializedName("url_n") val urlSmall320px: String?,
    @SerializedName("url_m") val urlSmall240px: String?,
    @SerializedName("url_q") val urlThumbnail150px: String?,
    @SerializedName("url_t") val urlThumbnail100px: String?,
    @SerializedName("url_s") val urlThumbnail75px: String?,
    @SerializedName("url_sq") val urlThumbnailSquare: String?
)

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
