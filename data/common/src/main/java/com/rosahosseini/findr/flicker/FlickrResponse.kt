package com.rosahosseini.findr.flicker

import com.google.gson.annotations.SerializedName
import com.rosahosseini.findr.ErrorManager
import com.rosahosseini.findr.model.ApiError

open class FlickrResponse<T>(
    open val data: T,
    @SerializedName("stat") internal open val status: String,
    @SerializedName("message") internal open val errorMassage: String?,
    @SerializedName("code") internal open val errorCode: Int?
) {
    private val isFailure get(): Boolean = status == "fail"

    fun getOrThrow(errorManager: ErrorManager): T {
        if (isFailure) {
            throw ApiError(
                errorType = errorManager.errorType(errorCode, ApiError.Type.Http),
                code = errorCode,
                throwable = Throwable(errorMassage)
            )
        }
        return data
    }
}
