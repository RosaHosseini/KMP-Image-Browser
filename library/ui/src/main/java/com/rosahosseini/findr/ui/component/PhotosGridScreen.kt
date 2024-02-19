package com.rosahosseini.findr.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotosGridScreen(
    photos: ImmutableList<Photo>,
    onPhotoClick: (Photo) -> Unit,
    onToggleBookmark: (Photo) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyGridState = rememberLazyGridState()
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        state = listState,
        modifier = modifier
    ) {
        items(
            photos,
            key = { item -> item.id },
            contentType = { "image-card" }
        ) { item ->
            PhotoCard(
                photo = item,
                onToggleBookmark = onToggleBookmark,
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(Dimensions.defaultMarginQuarter)
                    .clickable { onPhotoClick(item) }
            )
        }
    }
}

@Composable
private fun PhotoCard(
    photo: Photo,
    onToggleBookmark: (Photo) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LoadImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f),
            url = photo.thumbnailUrl ?: photo.url,
            description = photo.description
        )
        Bookmark(
            isBookmarked = { photo.isBookmarked },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(Dimensions.defaultMargin)
                .clip(CircleShape)
                .clickable { onToggleBookmark(photo) }
                .size(24.dp)
        )
        photo.title?.takeIf { it.isNotBlank() }?.let {
            TitleBar(
                title = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun TitleBar(title: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = title,
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
