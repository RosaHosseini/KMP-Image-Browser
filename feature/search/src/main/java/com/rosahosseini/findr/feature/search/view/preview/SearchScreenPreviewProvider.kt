package com.rosahosseini.findr.feature.search.view.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rosahosseini.findr.feature.search.viewmodel.SearchContract
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.state.PagingState
import kotlinx.collections.immutable.persistentListOf

internal class SearchScreenPreviewProvider : PreviewParameterProvider<SearchContract.State> {
    private val photo = Photo("1", "title", "desc", "", null)
    private val photos = persistentListOf(
        photo,
        photo.copy(id = "2"),
        photo.copy(id = "3"),
        photo.copy(id = "4")
    )
    private val suggestions = persistentListOf("term1", "term2", "term3")
    override val values: Sequence<SearchContract.State> = sequenceOf(
        SearchContract.State(
            photos = PagingState(
                data = persistentListOf(),
                status = PagingState.Status.Success
            ),
            suggestions = suggestions,
            term = ""
        ),
        SearchContract.State(
            photos = PagingState(
                data = photos,
                status = PagingState.Status.Success
            ),
            suggestions = suggestions,
            term = ""
        ),

        SearchContract.State(
            photos = PagingState(
                data = photos,
                status = PagingState.Status.Refreshing
            ),
            suggestions = suggestions,
            term = ""
        ),
        SearchContract.State(
            photos = PagingState(
                data = photos,
                status = PagingState.Status.Loading
            ),
            suggestions = suggestions,
            term = ""
        ),
        SearchContract.State(
            photos = PagingState(
                data = photos,
                status = PagingState.Status.Failure,
                throwable = Throwable("some error happened")
            ),
            suggestions = suggestions,
            term = ""
        )
    )
}
