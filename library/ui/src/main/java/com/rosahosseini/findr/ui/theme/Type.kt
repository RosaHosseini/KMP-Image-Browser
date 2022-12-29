package com.rosahosseini.findr.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    h1 = TextStyle(
        color = FindrColor.Silver,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 48.sp
    ),
    h2 = TextStyle(
        color = FindrColor.Silver,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    ),
    h3 = TextStyle(
        color = FindrColor.Silver,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    subtitle1 = TextStyle(
        color = FindrColor.TextDark,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        color = FindrColor.TextDark,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        letterSpacing = 0.15.sp
    ),
    button = TextStyle(
        color = FindrColor.Silver,
        fontWeight = FontWeight.Medium,
        fontFamily = FontFamily.Default,
        fontSize = 18.sp,
        letterSpacing = 1.25.sp
    )
)