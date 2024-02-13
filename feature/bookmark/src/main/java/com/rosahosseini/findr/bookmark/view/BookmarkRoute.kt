package com.rosahosseini.findr.bookmark.view

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rosahosseini.findr.bookmark.viewmodel.BookmarkViewModel
import com.rosahosseini.findr.feature.bookmark.R
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.component.PhotosGridScreen
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrColor
import com.rosahosseini.findr.library.ui.R as UiR

@Composable
fun BookmarkRoute(
    navigateToPhotoDetail: (
        url: String,
        title: String?,
        description: String?,
    ) -> Unit,
    onBackPressed: () -> Unit,
    bookmarkViewModel: BookmarkViewModel = hiltViewModel(),
) {
    val bookmarkedPhotos by bookmarkViewModel.bookmarkedPhotos.collectAsStateWithLifecycle()
    BookmarkScreen(
        photos = bookmarkedPhotos,
        onPhotoClick = { navigateToPhotoDetail(it.urlOriginal, it.title, it.description) },
        onToggleBookmark = bookmarkViewModel::onToggleBookmark,
        onBackPressed = onBackPressed
    )
}

@Composable
fun BookmarkScreen(
    photos: List<Photo>,
    onPhotoClick: (Photo) -> Unit,
    onToggleBookmark: (Photo) -> Unit,
    onBackPressed: () -> Unit,
) {
    Column(Modifier.background(FindrColor.DarkBackground)) {
        AppBar(onBackPressed)
        PhotosGridScreen(
            photos = photos,
            onPhotoClick = onPhotoClick,
            onToggleBookmark = onToggleBookmark,
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.defaultMarginHalf),
        )
    }
}

@Composable
private fun AppBar(onBackPressed: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = FindrColor.DarkBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.defaultElevation)
    ) {
        Row(
            Modifier
                .padding(Dimensions.defaultMarginDouble)
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
            Spacer(modifier = Modifier.width(Dimensions.defaultMarginDouble))
            Text(
                text = stringResource(id = R.string.bookmarks),
                style = MaterialTheme.typography.headlineSmall,
                color = FindrColor.TextLight,
                textAlign = TextAlign.Center
            )
        }
    }
}