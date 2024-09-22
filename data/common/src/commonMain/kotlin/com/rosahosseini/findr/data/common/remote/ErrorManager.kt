package com.rosahosseini.findr.data.common.remote

import com.rosahosseini.findr.domain.model.ApiError

fun interface ErrorManager {
    fun apiError(cause: Throwable): ApiError
}
