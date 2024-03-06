package com.rosahosseini.findr.model

data class ApiError(
    val errorType: Type,
    val code: Int? = null,
    private val throwable: Throwable? = null
) : Throwable(throwable?.message) {
    enum class Type {
        Unknown,
        Http,
        Unreachable,
        Security,
        Network,
        Server,
        Redirect,
        Information,
        UnAuthorized,
        Forbidden,
        NotFound,
        BadRequest,
        Client
    }
}
