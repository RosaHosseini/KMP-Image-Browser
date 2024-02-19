package com.rosahosseini.findr.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrColor
import com.rosahosseini.findr.ui.widget.LoadImage
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PhotosGridScreen(
    photos: ImmutableList<Photo>,
    onPhotoClick: (Photo) -> Unit,
    onToggleBookmark: (Photo) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyGridState = rememberLazyGridState()
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        state = listState,
        modifier = modifier
    ) {
        itemsIndexed(photos) { _, item ->
            key(item.id) {
                PhotoCard(item, onPhotoClick, onToggleBookmark)
            }
        }
    }
}

@Composable
private fun PhotoCard(
    photo: Photo,
    onPhotoClick: (Photo) -> Unit,
    onToggleBookmark: (Photo) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(Dimensions.defaultMarginQuarter)
            .clickable { onPhotoClick(photo) }
    ) {
        LoadImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f),
            url = photo.urlThumbnail,
            description = photo.description
        )
        Bookmark(
            isBookmarked = { photo.isBookmarked },
            onClick = { onToggleBookmark(photo) },
            modifier = Modifier
                .padding(Dimensions.defaultMargin)
                .align(Alignment.TopEnd)
                .size(24.dp)
        )
        TitleBar(photo.title)
    }
}

@Composable
private fun BoxScope.TitleBar(title: String?) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
    ) {
        Text(
            text = title?.takeIf { it.isNotBlank() }.orEmpty(),
            style = MaterialTheme.typography.titleSmall,
            color = FindrColor.TextLight,
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(FindrColor.DarkBackground, Color.Transparent),
                        startY = Float.POSITIVE_INFINITY,
                        endY = 0f
                    )
                )
                .padding(Dimensions.defaultMarginHalf)
                .fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}
