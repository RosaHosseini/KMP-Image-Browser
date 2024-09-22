package com.rosahosseini.findr.domain.model

data class ApiError(
    val code: Int? = null,
    private val throwable: Throwable?,
    override val message: String? = throwable?.message
) : Throwable() {
    enum class Type {
        Unknown,
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

    val type: Type = when (code) {
        401 -> Type.UnAuthorized
        403 -> Type.Forbidden
        404 -> Type.NotFound
        400 -> Type.BadRequest
        in 100..<200 -> Type.Information
        in 300..<400 -> Type.Redirect
        in 400..<500 -> Type.Client
        in 500..<600 -> Type.Server
        else -> when (throwable) {
//            is UnknownHostException, is SocketTimeoutException -> Type.Unreachable
//            is SSLException -> Type.Security
//            is Respon -> Type.Network
            else -> Type.Unknown
        }
    }
}
