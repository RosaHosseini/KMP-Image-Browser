package com.rosahosseini.findr.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.library.ui.R
import com.rosahosseini.findr.ui.preview.ComponentPreviews
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrTheme
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
            space = Dimensions.medium,
            alignment = Alignment.CenterVertically
        )
    ) {
        Text(
            text = message,
            style = Typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )

        if (onActionClick != null) {
            OutlinedButton(
                modifier = Modifier,
                onClick = onActionClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
            ) {
                Text(text = actionLabel, style = Typography.bodySmall)
            }
        }
    }
}

@ComponentPreviews
@Composable
private fun ErrorComponentPreview() {
    FindrTheme {
        ErrorComponent(
            message = "some error happened",
            onActionClick = {}
        )
    }
}
