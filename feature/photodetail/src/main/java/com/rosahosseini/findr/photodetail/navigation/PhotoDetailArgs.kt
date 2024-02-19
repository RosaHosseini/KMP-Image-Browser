package com.rosahosseini.findr.photodetail.navigation

import android.os.Bundle

data class PhotoDetailArgs(
    val url: String,
    val title: String?,
    val description: String?
) {
    constructor(bundle: Bundle) : this(
        requireNotNull(bundle.getString(ARG_URL)),
        bundle.getString(ARG_TITLE),
        bundle.getString(ARG_DESCRIPTION)
    )
}
