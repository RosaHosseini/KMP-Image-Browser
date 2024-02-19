package com.rosahosseini.findr.flicker

import com.google.gson.annotations.SerializedName

open class FlickrResponse<T>(
    open val data: T,
    @SerializedName("stat") internal open val status: String,
    @SerializedName("message") internal open val errorMassage: String?,
    @SerializedName("code") internal open val errorCode: Int?
) {
    private val isFailure get(): Boolean = status == "fail"

    fun toResult(): Result<T> {
        return if (isFailure) {
            Result.failure(FlickrException(errorMassage.orEmpty()))
        } else {
            Result.success(data)
        }
    }
}
