package com.rosahosseini.findr.data.common.remote.model

import com.rosahosseini.findr.domain.model.ApiError

interface FlickrResponse<T> {
    val data: T
    val stat: String
    val message: String?
    val code: Int?

    val isFailure get(): Boolean = stat == "fail"

    fun getOrThrow(): T {
        if (isFailure) {
            throw ApiError(code = code, throwable = Throwable(message))
        }
        return data
    }
}
