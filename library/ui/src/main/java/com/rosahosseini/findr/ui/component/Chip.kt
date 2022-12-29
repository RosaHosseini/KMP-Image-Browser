package com.rosahosseini.findr.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rosahosseini.findr.ui.theme.FindrColor
import com.rosahosseini.findr.ui.theme.Dimen
import com.rosahosseini.findr.ui.theme.Typography

@Composable
fun CancelableChip(
    modifier: Modifier = Modifier,
    tag: String,
    onClick: () -> Unit = {},
    onCancel: () -> Unit = {},
    backgroundColor: Color = FindrColor.Grey40,
    textColor: Color = FindrColor.TextDark
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onClick() }
                .padding(vertical = Dimen.defaultMarginHalf, horizontal = Dimen.defaultMarginOneHalf)
        ) {
            Text(
                text = tag,
                style = Typography.body2,
                modifier = Modifier.padding(end = Dimen.defaultMargin),
                color = textColor
            )
            Surface(color = Color.DarkGray, shape = CircleShape) {
                IconButton(
                    onClick = { onCancel() },
                    modifier = Modifier.size(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        tint = FindrColor.Grey90,
                        contentDescription = null
                    )
                }
            }
        }
    }
}