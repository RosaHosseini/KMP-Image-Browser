package com.rosahosseini.findr.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.rosahosseini.findr.library.ui.R
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrColor
import com.rosahosseini.findr.ui.theme.Typography

@Composable
fun ErrorComponent(
    message: String,
    onActionClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    actionLabel: String = stringResource(id = R.string.retry)
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = Dimensions.defaultMargin,
            alignment = Alignment.CenterVertically
        )
    ) {
        Text(
            text = message,
            style = Typography.bodyMedium,
            color = FindrColor.TextLight,
            textAlign = TextAlign.Center
        )

        if (onActionClick != null) {
            OutlinedButton(
                modifier = Modifier,
                onClick = onActionClick
            ) {
                Text(text = actionLabel, style = Typography.bodySmall)
            }
        }
    }
}
