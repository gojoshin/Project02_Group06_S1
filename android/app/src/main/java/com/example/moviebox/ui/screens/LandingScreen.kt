
package com.example.moviebox.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.material3.ExperimentalMaterial3Api


// LandingScreen (the Home tab)
// Layout, top to bottom:
//   1. Date line in mono ("THU / OCT 1"), same as my old project's header
//   2. Project's name in Big letters (MovieBox)
//   3. "Popular this week" row of posters you can swipe sideways
//   4. "Recently added" list, one movie per row with a thin line between


@OptIn(ExperimentalMaterial3Api::class)
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

    // State of filters
    var showFilters by remember { mutableStateOf(false) }

    var titleFilter by remember { mutableStateOf("") }
    var yearFilter by remember { mutableStateOf("") }
    var sortFilter by remember { mutableStateOf("Default") }

    var draftTitle by remember { mutableStateOf("") }
    var draftYear by remember { mutableStateOf("") }
    var draftSort by remember { mutableStateOf("Default") }

    // filtered movies
    val filteredMovies = movies.filter { movie ->
        movie.title.contains(titleFilter, ignoreCase = true) &&
                (yearFilter.isBlank() || movie.year.toString() == yearFilter)
    }.let { results ->
        when (sortFilter) {
            "Newest" -> results.sortedByDescending { it.year }
            "Oldest" -> results.sortedBy { it.year }
            "Title A-Z" -> results.sortedBy { it.title.lowercase() }
            "Title Z-A" -> results.sortedByDescending { it.title.lowercase() }
            else -> results
        }
    }

    // Panel for the filters
    if (showFilters) {
        ModalBottomSheet(
            onDismissRequest = { showFilters = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = ScreenSidePadding)
            ) {
                Text(
                    text = "Filters",
                    fontFamily = Display,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = draftTitle,
                    onValueChange = { draftTitle = it },
                    label = { Text("Movie title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = draftYear,
                    onValueChange = {
                        draftYear = it.filter { char -> char.isDigit() }.take(4)
                    },
                    label = { Text("Release year") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Sort by",
                    fontFamily = Mono,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(8.dp))

                listOf(
                    "Default",
                    "Newest",
                    "Oldest",
                    "Title A-Z",
                    "Title Z-A"
                ).forEach { option ->
                    OutlinedButton(
                        onClick = { draftSort = option },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (draftSort == option) "✓ $option" else option
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            titleFilter = ""
                            yearFilter = ""
                            sortFilter = "Default"

                            draftTitle = ""
                            draftYear = ""
                            draftSort = "Default"

                            showFilters = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Reset")
                    }

                    Button(
                        onClick = {
                            titleFilter = draftTitle.trim()
                            yearFilter = draftYear
                            sortFilter = draftSort
                            showFilters = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Apply")
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }

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

            // Button for filters
            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    draftTitle = titleFilter
                    draftYear = yearFilter
                    draftSort = sortFilter
                    showFilters = true
                }
            ) {
                Text("Filters")
            }

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
            items(filteredMovies) { movie ->
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
            for (movie in filteredMovies.sortedByDescending { it.year }.take(4)) {
                MovieListRow(movie = movie, onClick = { onOpenMovie(movie.id) })
                Hairline()
            }
        }
    }
}

// One movie as a simple text row: title on the left, year on the right
@Composable
private fun MovieListRow(movie: Movie, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
