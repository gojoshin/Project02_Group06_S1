package com.example.moviebox.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviebox.data.Movie
import com.example.moviebox.data.sampleMovies
import com.example.moviebox.ui.components.Hairline
import com.example.moviebox.ui.components.PosterBlock
import com.example.moviebox.ui.components.ScreenSidePadding
import com.example.moviebox.ui.components.SectionLabel
import com.example.moviebox.ui.theme.Display
import com.example.moviebox.ui.theme.Mono
import com.example.moviebox.ui.theme.MovieBoxTheme
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


// LandingScreen (the Home tab)
// Layout, top to bottom:
//   1. Date line in mono ("THU / OCT 1"), same as my old project's header
//   2. Project's name in Big letters (MovieBox)
//   3. "Popular this week" row of posters you can swipe sideways
//   4. "Recently added" list, one movie per row with a thin line between


@Composable
fun LandingScreen(
    movies: List<Movie> = sampleMovies,
    onOpenMovie: (Int) -> Unit = {}
) {
    // SHOWS THE CURRENT DATE AT THE TOP OF THE SCREEN
    val today = LocalDate.now()
    val weekday = today.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val month = today.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val dateText = "$weekday / $month ${today.dayOfMonth}".uppercase(Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(top = 56.dp, bottom = 40.dp)
    ) {
        // ----- Header -----
        Column(Modifier.padding(horizontal = ScreenSidePadding)) {
            Text(
                text = dateText,
                fontFamily = Mono,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = "MovieBox",
                fontFamily = Display,
                fontSize = 46.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Log your films. Rate them. Find something new.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(36.dp))
            SectionLabel("Popular this week")
            Spacer(Modifier.height(12.dp))
        }

        // ----- Sideways poster row -----
        // contentPadding lines the first poster up with the text above it,
        // but still lets the row scroll right up to the screen edge.
        LazyRow(
            contentPadding = PaddingValues(horizontal = ScreenSidePadding),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(movies) { movie ->
                PosterBlock(
                    movie = movie,
                    modifier = Modifier.width(112.dp),
                    onClick = { onOpenMovie(movie.id) }
                )
            }
        }

        // ----- Recently added list -----
        Column(Modifier.padding(horizontal = ScreenSidePadding)) {
            Spacer(Modifier.height(36.dp))
            SectionLabel("Recently added")
            Spacer(Modifier.height(8.dp))
            Hairline()

            // Newest first. For now "newest" just means the most recent year year. Will fix it.
            for (movie in movies.sortedByDescending { it.year }.take(4)) {
                MovieListRow(movie)
                Hairline()
            }
        }
    }
}

// One movie as a simple text row: title on the left, year on the right
@Composable
private fun MovieListRow(movie: Movie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = movie.title,
                fontFamily = Display,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = movie.director,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = movie.year.toString(),
            fontFamily = Mono,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
fun LandingScreenPreview() {
    MovieBoxTheme {
        LandingScreen()
    }
}
