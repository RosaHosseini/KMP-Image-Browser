import com.rosahosseini.findr.data.search.remote.response.SearchPhotosDto
import com.rosahosseini.findr.db.entity.PhotoEntity
import com.rosahosseini.findr.db.entity.SearchedPhotoEntity
import com.rosahosseini.findr.remote.model.PhotoDto

internal val photoDto = PhotoDto(
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

internal val searchDto = SearchPhotosDto(
    photos = listOf(photoDto, photoDto),
    pageNumber = 1,
    endPage = 10,
    pageSize = 5
)

internal val photo = PhotoEntity(
    photoId = "",
    isBookmarked = false,
    title = null,
    description = null,
    url = "",
    thumbnailUrl = null,
    timeStamp = 0
)

internal val searchedPhotoEntity = SearchedPhotoEntity(
    searchEntity = SearchEntity(
        "",
        offset = 0,
        photoId = "",
        timeStamp = System.currentTimeMillis(),
        hasMore = true
    ),
    photoEntity = photo
)

internal val outdatedSearchedPhotoEntity = SearchedPhotoEntity(
    searchEntity = SearchEntity(
        queryText = "",
        offset = 0,
        photoId = "",
        timeStamp = 0,
        hasMore = true
    ),
    photoEntity = photo
)
