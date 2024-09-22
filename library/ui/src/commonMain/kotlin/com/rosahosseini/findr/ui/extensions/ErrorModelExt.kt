package com.rosahosseini.findr.ui.extensions

import androidx.compose.runtime.Composable
import com.rosahosseini.findr.domain.model.ApiError
import com.rosahosseini.findr.library.ui.Res
import com.rosahosseini.findr.library.ui.error_noInternet
import com.rosahosseini.findr.library.ui.error_serverAccessForbidden
import com.rosahosseini.findr.library.ui.error_serverUnavailable
import com.rosahosseini.findr.library.ui.error_serverUnreachable
import com.rosahosseini.findr.library.ui.error_unexpected
import org.jetbrains.compose.resources.stringResource

val Throwable.localMessage
    @Composable get(): String {
        val genericMessage = stringResource(Res.string.error_unexpected)
        val apiError = this as? ApiError ?: return genericMessage
        return when (apiError.type) {
            ApiError.Type.Network -> stringResource(Res.string.error_noInternet)
            ApiError.Type.Server -> stringResource(Res.string.error_serverUnavailable)
            ApiError.Type.Unreachable -> stringResource(Res.string.error_serverUnreachable)
            ApiError.Type.Forbidden -> stringResource(Res.string.error_serverAccessForbidden)
            ApiError.Type.UnAuthorized,
            ApiError.Type.BadRequest,
            ApiError.Type.NotFound,
            ApiError.Type.Client
            -> apiError.message ?: genericMessage

            else -> genericMessage
        }
    }
