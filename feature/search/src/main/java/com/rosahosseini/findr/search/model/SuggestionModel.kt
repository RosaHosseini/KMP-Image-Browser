package com.rosahosseini.findr.search.model

data class SuggestionModel(val tag: String) {
    val id = tag.hashCode()
}