package com.rosahosseini.findr.remote.model.response

import com.google.gson.annotations.SerializedName
import com.rosahosseini.findr.remote.model.FlickrException

open class FlickrResponse(
    @SerializedName("stat") val status: String,
    @SerializedName("message") val errorMassage: String?,
    @SerializedName("code") val errorCode: Int?
) {
    fun isFailure(): Boolean = status == "fail"

    fun checkSuccess() {
        if (isFailure()) throw FlickrException(errorMassage.orEmpty())
    }
}
