package com.rosahosseini.bleacher.repositoryimpl

import com.rosahosseini.bleacher.local.database.entity.PhotoEntity
import com.rosahosseini.bleacher.local.database.entity.SearchEntity
import com.rosahosseini.bleacher.local.database.entity.SearchedPhotoEntity
import com.rosahosseini.bleacher.remote.model.response.PhotoDto
import com.rosahosseini.bleacher.remote.model.response.SearchPhotosDto
import com.rosahosseini.bleacher.remote.model.response.SearchResponseDto

val photoDto = PhotoDto(
    id = "", title = null, description = null, urlOriginal = null,
    urlLarge = null, urlMedium800px = null, urlMedium640px = null, urlSmall320px = null,
    urlSmall240px = null, urlThumbnail150px = null, urlThumbnail100px = null,
    urlThumbnail75px = null, urlThumbnailSquare = null
)

val searchDto = SearchPhotosDto(
    photos = listOf(photoDto, photoDto),
    pageNumber = 1,
    endPage = 10,
    pageSize = 5
)

val searchResponse = SearchResponseDto(
    searchDto,
    status = "success",
    errorManage = null,
    errorCode = null
)

val photo = PhotoEntity(
    photoId = "", isBookmarked = false, title = null, description = null, urlOriginal = null,
    urlLarge = null, urlMedium800px = null, urlMedium640px = null, urlSmall320px = null,
    urlSmall240px = null, urlThumbnail150px = null, urlThumbnail100px = null,
    urlThumbnail75px = null, urlThumbnailSquare = null, timeStamp = 0
)

val searchedPhotoEntity = SearchedPhotoEntity(
    searchEntity = SearchEntity(
        "",
        offset = 0,
        photoId = "",
        timeStamp = System.currentTimeMillis(),
        hasMore = true
    ),
    photoEntity = photo
)
val outdatedSearchedPhotoEntity = SearchedPhotoEntity(
    searchEntity = SearchEntity(
        queryText = "",
        offset = 0,
        photoId = "",
        timeStamp = 0,
        hasMore = true
    ),
    photoEntity = photo
)