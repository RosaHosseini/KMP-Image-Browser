package com.rosahosseini.bleacher.search.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.rosahosseini.bleacher.ui.R
import com.rosahosseini.bleacher.ui.theme.BleacherColor
import com.rosahosseini.bleacher.ui.theme.Dimen
import com.rosahosseini.bleacher.ui.theme.Typography

@Composable
fun SearchBar(onSearch: (String) -> Unit, modifier: Modifier) {
    var searchText by rememberSaveable { mutableStateOf("") }
    Card(
        elevation = Dimen.defaultElevation,
        modifier = modifier.clip(RoundedCornerShape(Dimen.defaultCornerRadius)),
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(BleacherColor.Grey10),
            textStyle = Typography.body1.copy(BleacherColor.TextLight),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch(searchText) }),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LeadingIcon { onSearch(searchText) }
                    Spacer(modifier = Modifier.width(Dimen.defaultMargin))
                    Box(Modifier.weight(1f)) {
                        innerTextField()
                    }
                }
            },
            cursorBrush = SolidColor(BleacherColor.Grey90)
        )
    }
}

@Composable
private fun LeadingIcon(onClick: () -> Unit) {
    Icon(
        modifier = Modifier
            .requiredSize(24.dp)
            .clickable { onClick() },
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
        contentDescription = "",
        tint = BleacherColor.Grey90
    )
}