package com.rosahosseini.findr.photodetail.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.library.ui.R
import com.rosahosseini.findr.photodetail.navigation.PhotoDetailArgs
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrColor
import com.rosahosseini.findr.ui.widget.LoadImage

@Composable
internal fun PhotoDetailScreen(photo: PhotoDetailArgs, onBackPressed: () -> Unit) {
    Box(Modifier.background(FindrColor.DarkBackground)) {
        LoadImage(
            modifier = Modifier.fillMaxSize(),
            url = photo.url,
            description = photo.description,
            contentScale = ContentScale.Crop
        )
        PhotoToolbar(onBackPressed)
        photo.title?.takeIf { it.isNotBlank() }?.let {
            PhotoInfo(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                title = it,
                description = photo.description
            )
        }
    }
}

@Composable
private fun PhotoToolbar(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier then Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(FindrColor.DarkBackground, Color.Transparent)
                )
            )
            .fillMaxWidth()
            .padding(Dimensions.defaultMarginDouble)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(24.dp)
                .clickable { onBackPressed() }
        )
    }
}

@Composable
private fun PhotoInfo(
    title: String,
    description: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(FindrColor.DarkBackground, Color.Transparent),
                    startY = Float.POSITIVE_INFINITY,
                    endY = 0f
                )
            )
            .padding(Dimensions.defaultMarginDouble)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = FindrColor.TextLight,
            modifier = Modifier.fillMaxWidth()
        )
        description?.let {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = FindrColor.TextLight,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
