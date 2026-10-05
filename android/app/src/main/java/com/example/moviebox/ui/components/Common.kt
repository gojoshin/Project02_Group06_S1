package com.example.moviebox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviebox.data.Movie
import com.example.moviebox.ui.theme.Display
import com.example.moviebox.ui.theme.Mono
import java.util.Locale

// Shared pieces so every screen looks the same. Building blocks

/** Left/right padding for every screen. */
val ScreenSidePadding = 24.dp

/** Big serif title at the top of a screen. */
@Composable
fun ScreenTitle(text: String) {
    Text(
        text = text,
        fontFamily = Display,
        fontSize = 34.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
}

/** Small uppercase gray label above a section. */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(Locale.getDefault()),
        fontSize = 10.sp,
        letterSpacing = 1.5.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}

/** 1dp divider. */
@Composable
fun Hairline(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outline)
    )
}

/**
 * Colored placeholder poster with the title and year. Set the width and the
 * height follows at a real poster's shape.
 */
@Composable
fun PosterBlock(
    movie: Movie,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val posterShape = RoundedCornerShape(4.dp)

    // TODO: swap the colored box for Coil's AsyncImage once movies have a posterUrl
    Box(
        modifier = modifier
            .aspectRatio(2f / 3f)
            .clip(posterShape)
            .background(Color(movie.posterColor))
            .border(1.dp, MaterialTheme.colorScheme.outline, posterShape)
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Column(Modifier.align(Alignment.BottomStart)) {
            Text(
                text = movie.title,
                fontFamily = Display,
                fontSize = 13.sp,
                lineHeight = 15.sp,
                color = Color.White,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = movie.year.toString(),
                fontFamily = Mono,
                fontSize = 9.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

/** Placeholder for tabs that aren't built yet, so the bottom bar still works. */
@Composable
fun ComingSoonScreen(title: String, note: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = ScreenSidePadding)
            .padding(top = 56.dp)
    ) {
        SectionLabel("Coming soon")
        Spacer(Modifier.height(10.dp))
        ScreenTitle(title)
        Spacer(Modifier.height(8.dp))
        Text(
            text = note,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}