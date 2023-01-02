package com.rosahosseini.findr.repository.utils

import com.rosahosseini.findr.core.extensions.orNullIfEmpty
import com.rosahosseini.findr.model.ErrorModel
import com.rosahosseini.findr.remote.model.response.FlickrException
import com.rosahosseini.findr.repository.R
import java.io.IOException
import retrofit2.HttpException

internal interface ErrorManager {
    fun errorModel(error: Throwable): ErrorModel
}

internal class RemoteErrorManager : ErrorManager {
    override fun errorModel(error: Throwable): ErrorModel {
        return when (error) {
            is HttpException -> ErrorModel(
                type = ErrorModel.Type.HTTP,
                message = error.message(),
                localMessage = R.string.unexpected,
                code = error.code()
            )
            is FlickrException -> ErrorModel(
                type = ErrorModel.Type.HTTP,
                message = error.message.orNullIfEmpty()
            )
            is IOException -> ErrorModel(
                type = ErrorModel.Type.NETWORK,
                message = error.message?.orNullIfEmpty(),
                localMessage = R.string.network_connection
            )
            else -> ErrorModel(
                type = ErrorModel.Type.UNEXPECTED,
                message = error.message,
                localMessage = R.string.unexpected
            )
        }
    }
}