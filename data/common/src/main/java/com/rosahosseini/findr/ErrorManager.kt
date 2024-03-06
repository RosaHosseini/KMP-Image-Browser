package com.rosahosseini.findr

import com.rosahosseini.findr.model.ApiError.Type

interface ErrorManager {
    fun apiError(throwable: Throwable): Throwable
    fun errorType(throwable: Throwable): Type
    fun errorType(errorCode: Int?, default: Type): Type
}
