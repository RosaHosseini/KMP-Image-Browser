package com.rosahosseini.findr.photodetail.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.R
import com.rosahosseini.findr.ui.theme.FindrColor
import com.rosahosseini.findr.ui.theme.Dimen
import com.rosahosseini.findr.ui.theme.Typography
import com.rosahosseini.findr.ui.widget.LoadImage

@Composable
fun PhotoDetailRoute(photo: Photo, onBackPressed: () -> Unit) {
    PhotoDetailScreen(photo, onBackPressed)
}

@Composable
fun PhotoDetailScreen(photo: Photo, onBackPressed: () -> Unit) {
    Box(Modifier.background(FindrColor.DarkBackground)) {
        LoadImage(
            modifier = Modifier
                .fillMaxSize(),
            url = photo.urlLarge,
            description = photo.description,
            contentScale = ContentScale.FillBounds
        )
        TopTool(onBackPressed)
        BottomContent(photo)
    }
}

@Composable
private fun BoxScope.TopTool(onBackPressed: () -> Unit) {
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(FindrColor.DarkBackground, Color.Transparent)
                )
            )
            .fillMaxWidth()
            .padding(Dimen.defaultMarginDouble)
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
private fun BoxScope.BottomContent(photo: Photo) {
    photo.title?.let { title ->
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(FindrColor.DarkBackground, Color.Transparent),
                        startY = Float.POSITIVE_INFINITY,
                        endY = 0f
                    )
                )
                .padding(Dimen.defaultMarginDouble)
        ) {
            Text(
                text = title,
                style = Typography.h3,
                color = FindrColor.TextLight,
                modifier = Modifier
                    .fillMaxWidth(),
            )
            photo.description?.let { description ->
                Text(
                    text = description,
                    style = Typography.body2,
                    color = FindrColor.TextLight,
                    modifier = Modifier
                        .alpha(1f)
                        .fillMaxWidth(),
                )
            }
        }
    }
}