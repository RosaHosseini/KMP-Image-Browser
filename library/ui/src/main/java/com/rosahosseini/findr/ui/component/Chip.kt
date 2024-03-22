package com.rosahosseini.findr.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.ui.theme.Dimensions
import com.rosahosseini.findr.ui.theme.FindrTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancelableChip(
    tag: String,
    onClick: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimary
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        ElevatedAssistChip(
            onClick = onClick,
            label = { Text(text = tag, style = MaterialTheme.typography.bodySmall) },
            modifier = modifier,
            enabled = true,
            trailingIcon = {
                IconButton(
                    onClick = { onCancel() },
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.padding(Dimensions.xSmall)
                    )
                }
            },
            shape = CircleShape,
            colors = AssistChipDefaults.assistChipColors(labelColor = color),
            border = AssistChipDefaults.assistChipBorder(enabled = true, borderColor = color),
            elevation = AssistChipDefaults.assistChipElevation()
        )
    }
}

@Preview
@Composable
private fun CancelableChipPreview() {
    FindrTheme {
        CancelableChip(
            tag = "label",
            onClick = {},
            onCancel = {}
        )
    }
}
