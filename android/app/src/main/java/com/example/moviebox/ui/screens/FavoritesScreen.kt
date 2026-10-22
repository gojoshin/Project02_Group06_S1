package com.example.moviebox.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.moviebox.ui.components.ScreenTitle
import com.example.moviebox.ui.components.SectionLabel
import com.example.moviebox.ui.theme.Display
import com.example.moviebox.ui.theme.Mono
import com.example.moviebox.ui.theme.MovieBoxTheme

/**
 * Favorites tab. Lists every movie the user hearted. Tapping a row opens the
 * movie, and tapping the heart removes it.
 */
@Composable
fun FavoritesScreen(
    favoriteMovies: List<Movie>,
    onOpenMovie: (Int) -> Unit = {},
    onRemoveFavorite: (Int) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = ScreenSidePadding)
            .padding(top = 56.dp, bottom = 40.dp)
    ) {
        SectionLabel("Your list")
        Spacer(Modifier.height(10.dp))
        ScreenTitle("Favorites")
        Spacer(Modifier.height(24.dp))

        // Nothing saved yet
        if (favoriteMovies.isEmpty()) {
            Text(
                text = "No favorites yet. Open a movie and tap \"Add to favorites\".",
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            return@Column
        }

        Hairline()
        for (movie in favoriteMovies) {
            FavoriteRow(
                movie = movie,
                onClick = { onOpenMovie(movie.id) },
                onRemove = { onRemoveFavorite(movie.id) }
            )
            Hairline()
        }
    }
}

// One favorite: small poster, title and director, then a red heart to remove it
@Composable
private fun FavoriteRow(movie: Movie, onClick: () -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PosterBlock(
            movie = movie,
            modifier = Modifier.width(48.dp),
            onClick = onClick
        )

        Spacer(Modifier.width(14.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = movie.title,
                fontFamily = Display,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "${movie.director} • ${movie.year}",
                fontFamily = Mono,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Remove ${movie.title} from favorites",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 700)
@Composable
fun FavoritesScreenPreview() {
    MovieBoxTheme {
        FavoritesScreen(favoriteMovies = sampleMovies.take(3))
    }
}