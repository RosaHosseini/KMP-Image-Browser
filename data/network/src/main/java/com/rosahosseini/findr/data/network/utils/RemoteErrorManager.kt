package com.rosahosseini.findr.data.network.utils

import com.rosahosseini.findr.ErrorManager
import com.rosahosseini.findr.model.ApiError
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLException
import retrofit2.HttpException

internal class RemoteErrorManager @Inject constructor() : ErrorManager {

    override fun apiError(throwable: Throwable): ApiError {
        return ApiError(errorType(throwable), errorCode(throwable), throwable)
    }

    override fun errorType(throwable: Throwable): ApiError.Type {
        val errorCode = errorCode(throwable)
        return when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> ApiError.Type.Unreachable
            is SSLException -> ApiError.Type.Security
            is IOException -> ApiError.Type.Network
            is HttpException -> errorType(errorCode, ApiError.Type.Http)
            else -> ApiError.Type.Unknown
        }
    }

    override fun errorType(errorCode: Int?, default: ApiError.Type): ApiError.Type {
        return when (errorCode) {
            401 -> ApiError.Type.UnAuthorized
            403 -> ApiError.Type.Forbidden
            404 -> ApiError.Type.NotFound
            400 -> ApiError.Type.BadRequest
            in 100..<200 -> ApiError.Type.Information
            in 300..<400 -> ApiError.Type.Redirect
            in 400..<500 -> ApiError.Type.Client
            in 500..<600 -> ApiError.Type.Server
            else -> default
        }
    }

    private fun errorCode(error: Throwable) = (error as? HttpException)?.code()
}
