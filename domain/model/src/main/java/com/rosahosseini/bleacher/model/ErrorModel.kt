package com.rosahosseini.bleacher.model

data class ErrorModel(
    val type: Type = Type.UNEXPECTED,
    val message: String? = null,
    val localMessage: Int? = null,
    val code: Int? = null
) {
    enum class Type {
        HTTP,
        NETWORK,
        UNEXPECTED,
        EMPTY_DATA,
        DB
    }
}