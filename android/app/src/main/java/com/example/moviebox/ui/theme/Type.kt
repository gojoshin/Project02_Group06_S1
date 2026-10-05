package com.example.moviebox.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Fonts:
// Everything should be default so there's nothing to download or add to res/font:
//   - Display (Serif)     -> big titles like movie names and screen headers
//   - Mono (Monospace)    -> numbers, dates, years, usernames
//   - FontFamily.Default  -> everything else (normal body text)
// ---------------------------------------------------------------------------

val Display = FontFamily.Serif
val Mono = FontFamily.Monospace

// Material's default text style. Most of our screens set fontSize directly
// anything that doesn't, will fall back / defaulrt to this.
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)
