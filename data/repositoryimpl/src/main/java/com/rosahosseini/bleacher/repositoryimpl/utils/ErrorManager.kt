package com.rosahosseini.bleacher.repositoryimpl.utils

import com.rosahosseini.bleacher.core.extensions.orNullIfEmpty
import com.rosahosseini.bleacher.model.ErrorModel
import com.rosahosseini.bleacher.remote.model.response.FlickrException
import com.rosahosseini.bleacher.repositoryimpl.R
import java.io.IOException
import retrofit2.HttpException

interface ErrorManager {
    fun errorModel(error: Throwable): ErrorModel
}

class RemoteErrorManager : ErrorManager {
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