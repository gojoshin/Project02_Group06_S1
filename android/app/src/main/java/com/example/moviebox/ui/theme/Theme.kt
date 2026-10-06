package com.example.moviebox.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// ---------------------------------------------------------------------------
// MovieBoxTheme
//
// Wrap every screen (and every @Preview) in MovieBoxTheme { ... } so it gets
// our colors. Inside a screen, read colors with MaterialTheme.colorScheme.xxx
// instead of using the raw values from Color.kt. Quick cheat sheet:
//
//   colorScheme.background        -> Charcoal   (screen background)
//   colorScheme.surface           -> CharcoalSurface (tab bar, dialogs)
//   colorScheme.surfaceVariant    -> CharcoalRaised (posters, cards)
//   colorScheme.onBackground      -> Ink        (main text)
//   colorScheme.onSurfaceVariant  -> InkMuted   (labels, hints)
//   colorScheme.outline           -> Hairline   (dividers)
//   colorScheme.secondary         -> StarGold   (stars)
//   colorScheme.error             -> HeartRed   (hearts, errors)
//
// We always use the dark scheme, even if the phone is in light mode.
// We also turned OFF Android 12 "dynamic color" so the app looks the same on
// every phone (otherwise it would recolor itself to match the wallpaper).
// ---------------------------------------------------------------------------

private val MovieBoxColors = darkColorScheme(
    primary = Ink,
    onPrimary = Charcoal,
    secondary = StarGold,
    onSecondary = Charcoal,
    background = Charcoal,
    onBackground = Ink,
    surface = CharcoalSurface,
    onSurface = Ink,
    surfaceVariant = CharcoalRaised,
    onSurfaceVariant = InkMuted,
    outline = Hairline,
    error = HeartRed
)

@Composable
fun MovieBoxTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MovieBoxColors,
        typography = Typography,
        content = content
    )
}
