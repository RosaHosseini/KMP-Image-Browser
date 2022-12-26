package com.rosahosseini.bleacher.repositoryimpl.map

import com.rosahosseini.bleacher.core.extensions.orNullIfEmpty
import com.rosahosseini.bleacher.local.database.entity.PhotoEntity
import com.rosahosseini.bleacher.model.Photo
import com.rosahosseini.bleacher.remote.model.response.PhotoDto

fun PhotoDto.toPhoto() = Photo(
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

fun Photo.toPhotoEntity() = PhotoEntity(
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
    urlThumbnailSquare
)

fun PhotoEntity.toPhoto() = Photo(
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