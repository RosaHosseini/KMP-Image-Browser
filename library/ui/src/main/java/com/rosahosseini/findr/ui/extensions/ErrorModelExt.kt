package com.rosahosseini.findr.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rosahosseini.findr.library.ui.R
import com.rosahosseini.findr.model.ErrorModel

val ErrorModel.localMessage
    @Composable get(): String = when (type) {
        ErrorModel.Type.HTTP -> message ?: stringResource(R.string.an_error_happened)
        ErrorModel.Type.NETWORK -> stringResource(R.string.network_connection)
        ErrorModel.Type.UNEXPECTED -> stringResource(R.string.unexpected)
        ErrorModel.Type.EMPTY_DATA, ErrorModel.Type.DB -> stringResource(R.string.an_error_happened)
    }
