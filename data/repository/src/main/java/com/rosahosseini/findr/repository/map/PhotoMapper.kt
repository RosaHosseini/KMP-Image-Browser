package com.rosahosseini.findr.repository.map

import com.rosahosseini.findr.core.extensions.orNullIfEmpty
import com.rosahosseini.findr.local.database.entity.PhotoEntity
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.remote.model.response.PhotoDto

internal fun PhotoDto.toPhoto() = Photo(
    id,
    isBookmarked = false,
    title,
    description?.orNullIfEmpty(),
    urlOriginal?.orNullIfEmpty(),
    urlLarge?.orNullIfEmpty(),
    urlMedium800px?.orNullIfEmpty(),
    urlMedium640px?.orNullIfEmpty(),
    urlSmall320px?.orNullIfEmpty(),
    urlSmall240px?.orNullIfEmpty(),
    urlThumbnail150px?.orNullIfEmpty(),
    urlThumbnail100px?.orNullIfEmpty(),
    urlThumbnail75px?.orNullIfEmpty(),
    urlThumbnailSquare?.orNullIfEmpty()
)

internal fun Photo.toPhotoEntity(timeStamp: Long) = PhotoEntity(
    id,
    isBookmarked,
    title,
    description,
    urlOriginal,
    urlLargeNullable,
    urlMedium800px,
    urlMedium640px,
    urlSmall320px,
    urlSmall240px,
    urlThumbnail150px,
    urlThumbnail100px,
    urlThumbnail75px,
    urlThumbnailSquare,
    timeStamp
)

internal fun PhotoEntity.toPhoto() = Photo(
    photoId,
    isBookmarked,
    title,
    description,
    urlOriginal,
    urlLarge,
    urlMedium800px,
    urlMedium640px,
    urlSmall320px,
    urlSmall240px,
    urlThumbnail150px,
    urlThumbnail100px,
    urlThumbnail75px,
    urlThumbnailSquare
)