package com.rosahosseini.findr.repository.utils

import com.rosahosseini.findr.extensions.orNullIfEmpty
import com.rosahosseini.findr.flicker.FlickrException
import com.rosahosseini.findr.model.ErrorModel
import java.io.IOException
import retrofit2.HttpException

interface ErrorManager {
    fun errorModel(error: Throwable): ErrorModel
}

internal class RemoteErrorManager : ErrorManager {
    override fun errorModel(error: Throwable): ErrorModel {
        return when (error) {
            is HttpException -> ErrorModel(
                type = ErrorModel.Type.HTTP,
                message = error.message()
            )

            is FlickrException -> ErrorModel(
                type = ErrorModel.Type.HTTP,
                message = error.message.orNullIfEmpty()
            )

            is IOException -> ErrorModel(
                type = ErrorModel.Type.NETWORK,
                message = error.message?.orNullIfEmpty()
            )

            else -> ErrorModel(
                type = ErrorModel.Type.UNEXPECTED,
                message = error.message
            )
        }
    }
}
