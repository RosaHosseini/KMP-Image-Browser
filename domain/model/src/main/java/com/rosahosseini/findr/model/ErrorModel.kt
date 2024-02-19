package com.rosahosseini.findr.model

data class ErrorModel(
    val type: Type = Type.UNEXPECTED,
    val message: String? = null
) {
    enum class Type {
        HTTP,
        NETWORK,
        UNEXPECTED,
        EMPTY_DATA,
        DB
    }
}
