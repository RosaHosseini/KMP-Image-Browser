package com.rosahosseini.findr.feature.search.viewmodel

import com.rosahosseini.findr.model.Page
import com.rosahosseini.findr.model.Photo
import com.rosahosseini.findr.ui.state.PagingState
import javax.annotation.concurrent.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal object SearchContract {
    sealed interface Intent {
        data class OnTermChange(val term: String) : Intent

        data class OnRemoveSuggestion(val term: String) : Intent

        data object OnLoadMore : Intent
    }

    sealed interface Mutation {
        data class UpdateTerm(val term: String) : Mutation

        data class UpdateSuggestions(val suggestions: List<String>) : Mutation

        data class RemoveSuggestion(val term: String) : Mutation

        data class UpdatePhotos(val result: Result<Page<Photo>>) : Mutation
        data object LoadPhotos : Mutation
    }

    @Immutable
    data class State(
        val photos: PagingState<Photo> = PagingState(),
        val suggestions: ImmutableList<String> = persistentListOf(),
        val term: String = ""
    )
}
