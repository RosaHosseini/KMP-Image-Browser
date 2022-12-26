package com.rosahosseini.bleacher.bookmark.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rosahosseini.bleacher.bookmark.R
import com.rosahosseini.bleacher.bookmark.viewmodel.BookmarkViewModel
import com.rosahosseini.bleacher.model.Photo
import com.rosahosseini.bleacher.ui.component.PhotosGridScreen
import com.rosahosseini.bleacher.ui.theme.BleacherColor
import com.rosahosseini.bleacher.ui.theme.Dimen
import com.rosahosseini.bleacher.ui.theme.Typography
import com.rosahosseini.bleacher.ui.R as UiR

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun BookmarkRoute(bookmarkViewModel: BookmarkViewModel = hiltViewModel()) {
    val bookmarkedPhotos by bookmarkViewModel.bookmarkedPhotos.collectAsStateWithLifecycle()
    BookmarkScreen(
        photos = bookmarkedPhotos,
        onPhotoClick = bookmarkViewModel::onPhotoClick,
        onToggleBookmark = bookmarkViewModel::onToggleBookmark,
        onBackPressed = bookmarkViewModel::onBackPressed
    )
}

@Composable
fun BookmarkScreen(
    photos: List<Photo>,
    onPhotoClick: (Photo) -> Unit,
    onToggleBookmark: (Photo) -> Unit,
    onBackPressed: () -> Unit
) {
    Column(Modifier.background(BleacherColor.DarkBackground)) {
        AppBar(onBackPressed)
        PhotosGridScreen(
            photos = photos,
            onPhotoClick = onPhotoClick,
            onToggleBookmark = onToggleBookmark,
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimen.defaultMarginHalf),
        )
    }
}

@Composable
private fun AppBar(onBackPressed: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = BleacherColor.DarkBackground,
        elevation = Dimen.defaultElevation
    ) {
        Row(
            Modifier
                .padding(Dimen.defaultMarginDouble)
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = UiR.drawable.ic_back),
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackPressed() }
            )
            Spacer(modifier = Modifier.width(Dimen.defaultMarginDouble))
            Text(
                text = stringResource(id = R.string.bookmarks),
                style = Typography.h3,
                color = BleacherColor.TextLight,
                textAlign = TextAlign.Center
            )
        }
    }
}