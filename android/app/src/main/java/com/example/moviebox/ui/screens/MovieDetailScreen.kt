package com.example.moviebox.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviebox.data.sampleMovies
import com.example.moviebox.data.sampleProviders
import com.example.moviebox.ui.components.ScreenSidePadding
import com.example.moviebox.ui.components.SectionLabel
import com.example.moviebox.ui.theme.Display
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import com.example.moviebox.ui.components.PosterBlock
import com.example.moviebox.data.Movie
import com.example.moviebox.data.MovieRetrofit
import com.example.moviebox.data.toMovie
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: Int,
    onBack: () -> Unit,
    isFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {}
) {
    var movie by remember { mutableStateOf(sampleMovies.find { it.id == movieId }) }
    var isLoading by rememberSaveable { mutableStateOf(movie == null) }

    LaunchedEffect(movieId) {
        if (movie == null) {
            try {
                val apiMovie = MovieRetrofit.api.getMovie(movieId.toLong())
                movie = apiMovie.toMovie()
            } catch (e: Exception) {
                // Fallback or leave as null
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = ScreenSidePadding, vertical = 16.dp)
        ) {
            val safeMovie = movie
            
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (safeMovie == null) {
                Text("Movie not found")
            } else {
                // Poster on the left, title and info on the right
                Row(verticalAlignment = Alignment.Bottom) {
                    // Same poster as the landing page. Falls back to the colored
                    // box if the movie has no posterUrl.
                    PosterBlock(
                        movie = safeMovie,
                        modifier = Modifier.width(130.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = safeMovie.title,
                            fontFamily = Display,
                            fontSize = 28.sp,
                            lineHeight = 32.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${safeMovie.year} • Directed by ${safeMovie.director}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Favorite button. Filled red heart when saved, outline when not.
                OutlinedButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isFavorite) "Favorited" else "Add to favorites",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = safeMovie.title,
                    fontFamily = Display,
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "${safeMovie.year} • Directed by ${safeMovie.director}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Streaming Providers section
                SectionLabel("Where to watch")
                Spacer(Modifier.height(12.dp))

                if (sampleProviders.isEmpty()) {
                    Text(
                        text = "Not available to stream",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(sampleProviders) { provider ->
                            ProviderBadge(provider.name)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProviderBadge(name: String) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
