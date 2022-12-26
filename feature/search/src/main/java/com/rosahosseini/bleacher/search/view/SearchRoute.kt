package com.rosahosseini.bleacher.search.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rosahosseini.bleacher.model.Photo
import com.rosahosseini.bleacher.search.view.components.LoadingScreen
import com.rosahosseini.bleacher.search.view.components.SearchBar
import com.rosahosseini.bleacher.search.viewmodel.PhotoSearchViewModel
import com.rosahosseini.bleacher.ui.R
import com.rosahosseini.bleacher.ui.component.PhotosGridScreen
import com.rosahosseini.bleacher.ui.extensions.OnBottomReached
import com.rosahosseini.bleacher.ui.theme.BleacherColor
import com.rosahosseini.bleacher.ui.theme.Dimen
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchRoot(
    searchViewModel: PhotoSearchViewModel = hiltViewModel(),
) {
    val searchedPhotos by searchViewModel.searchedPhotosFlow.collectAsStateWithLifecycle()
    val isLoading by searchViewModel.loadingFlow.collectAsStateWithLifecycle()
    val error by searchViewModel.errorFlow.collectAsStateWithLifecycle()
    val errorMessage = error?.localMessage?.let { stringResource(it) } ?: error?.message
    val listState = rememberLazyGridState()
    listState.OnBottomReached(buffer = 1) {
        searchViewModel.onLoadMore()
    }
    LaunchedEffect(key1 = Unit) {
        searchViewModel.scrollToTop.collectLatest {
            listState.scrollToItem(0)
        }
    }
    val context = LocalContext.current
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(
                context, it, Toast.LENGTH_LONG
            ).show()
        }
    }
    SearchScreen(
        searchedPhotos,
        { isLoading },
        listState,
        searchViewModel::onQueryTextChange,
        searchViewModel::onPhotoClick,
        searchViewModel::onToggleBookmark,
        searchViewModel::onBookmarksClick,
    )
}

@Composable
private fun SearchScreen(
    photos: List<Photo>,
    isLoading: () -> Boolean,
    listState: LazyGridState,
    onQueryTextChange: (String) -> Unit,
    onPhotoClick: (Photo) -> Unit,
    onToggleBookmark: (Photo) -> Unit,
    onBookmarksClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BleacherColor.DarkBackground)
    ) {
        val alphaList = if (isLoading()) 0.8f else 1f
        Column {
            Card(elevation = Dimen.defaultElevation) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .background(BleacherColor.DarkBackground)
                        .padding(Dimen.defaultMarginHalf)
                ) {
                    SearchBar(
                        onQueryTextChange,
                        Modifier
                            .padding(Dimen.defaultMargin)
                            .weight(weight = 1f, fill = true)
                    )
                    BookmarksButton(onBookmarksClick)
                    Spacer(modifier = Modifier.size(Dimen.defaultMargin))
                }
            }
            PhotosGridScreen(
                photos,
                onPhotoClick,
                onToggleBookmark,
                Modifier.alpha(alphaList),
                listState
            )
        }
        if (isLoading()) LoadingScreen()
    }
}

@Composable
fun BookmarksButton(onBookmarksClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.ic_heart),
        contentDescription = stringResource(id = R.string.bookmark),
        modifier = Modifier
            .fillMaxHeight()
            .widthIn(min = 36.dp)
            .clickable { onBookmarksClick() }
    )
}