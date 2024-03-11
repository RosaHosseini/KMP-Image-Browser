package com.rosahosseini.findr.data.network

import com.rosahosseini.findr.model.ApiError
import javax.inject.Inject
import retrofit2.HttpException

internal class RemoteErrorManager @Inject constructor() {

    fun apiError(throwable: Throwable): ApiError {
        return ApiError(
            code = errorCode(throwable),
            throwable = throwable
        )
    }

    private fun errorCode(error: Throwable) = (error as? HttpException)?.code()
}
