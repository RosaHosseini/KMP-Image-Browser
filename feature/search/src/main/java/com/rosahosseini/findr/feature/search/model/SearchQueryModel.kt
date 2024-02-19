package com.rosahosseini.findr.feature.search.model

internal data class SearchQueryModel(
    val pageNumber: Int = 0,
    val text: String? = null
) {
    val nextPageQuery: SearchQueryModel
        get() = SearchQueryModel(pageNumber + 1, text)
}
