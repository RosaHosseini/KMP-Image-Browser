package com.rosahosseini.findr.search.model

data class SearchQueryModel(
    val pageNumber: Int = 0,
    val text: String? = null
) {
    val nextPageQuery: SearchQueryModel
        get() = SearchQueryModel(pageNumber + 1, text)
}