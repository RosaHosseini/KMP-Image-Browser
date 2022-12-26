package com.rosahosseini.bleacher.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val isBookmarked: Boolean,
    val title: String?,
    val description: String?,
    val urlOriginal: String?,
    val urlLargeNullable: String?,
    val urlMedium800px: String?,
    val urlMedium640px: String?,
    val urlSmall320px: String?,
    val urlSmall240px: String?,
    val urlThumbnail150px: String?,
    val urlThumbnail100px: String?,
    val urlThumbnail75px: String?,
    val urlThumbnailSquare: String?
) : Parcelable {
    val urlSmall: String
        get() = urlSmall240px ?: urlSmall320px ?: urlMedium

    val urlMedium: String
        get() = urlMedium640px ?: urlMedium800px ?: urlLarge

    val urlLarge: String
        get() = urlLargeNullable ?: urlOriginal.orEmpty()

    val urlThumbnail: String
        get() = urlThumbnail150px ?: urlThumbnail100px ?: urlThumbnail75px ?: urlThumbnailSquare
        ?: urlSmall
}