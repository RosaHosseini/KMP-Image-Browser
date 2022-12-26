package com.rosahosseini.bleacher.model

sealed class Either<out T>(open val data: T?) {
    class Success<T>(data: T) : Either<T>(data)
    class Error<T>(val error: ErrorModel, override val data: T? = null) : Either<T>(data)
    class Loading<T>(data: T? = null) : Either<T>(data)
}

fun <T> Either<T>.hasData(): Boolean {
    return data != null
}

fun <T> Either<T>.isFailure(): Boolean {
    return this is Either.Error
}

fun <T> Either<T>.isSuccess(): Boolean {
    return this is Either.Success
}

fun <T> Either<T>.isLoading(): Boolean {
    return this is Either.Loading
}

fun <T> Either<T>.getError(): ErrorModel? {
    return if (this is Either.Error)
        return error
    else null
}

fun <T, R> Either<T>.map(mapper: T.() -> R): Either<R> {
    return when (this) {
        is Either.Loading -> Either.Loading(data?.mapper())
        is Either.Success -> Either.Success(requireNotNull(data).mapper())
        is Either.Error -> Either.Error(error, data?.mapper())
    }
}