package com.example.moviebox.ui.theme

import androidx.compose.ui.graphics.Color

// ---------------------------------------------------------------------------
// MovieBox color palette
//
// The whole app is dark mode only. Almost everything is charcoal + white,
// with ONE warm accent (StarGold) used for ratings, and a red used only for
// the favorite heart. Keeping the accents rare is what makes the UI look clean.
//
// If you need a new color, add it here instead of hardcoding Color(0xFF...)
// inside a screen. That way the whole team pulls from the same palette.
// ---------------------------------------------------------------------------

// Backgrounds, darkest to lightest
val Charcoal = Color(0xFF121212)       // main screen background
val CharcoalSurface = Color(0xFF1B1B1B) // tab bar, dialogs
val CharcoalRaised = Color(0xFF262626)  // poster placeholders, input fills

// Text colors
val Ink = Color(0xFFF2F2F2)        // main text (almost white, easier on the eyes)
val InkMuted = Color(0xFF8E8E8E)   // labels, hints, secondary text

// Thin lines (dividers, unselected tab dots, borders)
val Hairline = Color(0xFF2E2E2E)

// Accents (use sparingly!)
val StarGold = Color(0xFFE8B04B)   // filled rating stars
val HeartRed = Color(0xFFE5534B)   // favorite heart + error text
