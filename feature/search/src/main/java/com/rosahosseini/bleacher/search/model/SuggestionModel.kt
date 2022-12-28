package com.rosahosseini.bleacher.search.model

data class SuggestionModel(val tag: String) {
    val id = tag.hashCode()
}