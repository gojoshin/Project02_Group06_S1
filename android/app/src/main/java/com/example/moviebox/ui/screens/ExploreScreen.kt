package com.example.moviebox.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviebox.data.Movie
import com.example.moviebox.data.MovieRetrofit
import com.example.moviebox.data.toMovie
import com.example.moviebox.ui.components.PosterBlock
import com.example.moviebox.ui.components.ScreenSidePadding
import com.example.moviebox.ui.theme.MovieBoxTheme
import kotlinx.coroutines.launch

/**
 * Explore tab. Shows TMDB's popular movies from our API, 20 at a time, and
 * loads the next page as you scroll down. Searching asks the API for matching
 * titles instead. Tapping a poster opens the movie, same as the landing page.
 */
@Composable
fun ExploreScreen(
    onOpenMovie: (Int) -> Unit = {}
) {
    // What's typed in the box vs. what we last searched for. They're separate
    // so we only hit the API when the user presses search, not on every letter.
    var searchText by rememberSaveable { mutableStateOf("") }
    var activeSearch by rememberSaveable { mutableStateOf("") }

    // Every movie loaded so far, across all pages
    val movies = remember { mutableStateListOf<Movie>() }

    // Paging info. currentPage = 0 means nothing has loaded yet.
    var currentPage by remember { mutableStateOf(0) }
    var totalPages by remember { mutableStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }
    var loadError by remember { mutableStateOf<String?>(null) }

    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // Gets the next page and adds it to the end of the list
    fun loadNextPage() {
        if (isLoading || currentPage >= totalPages) return

        isLoading = true
        loadError = null
        val pageToLoad = currentPage + 1
        val searchForThisRequest = activeSearch

        scope.launch {
            try {
                val result = MovieRetrofit.api.getMovies(
                    page = pageToLoad,
                    query = searchForThisRequest.ifBlank { null }
                )

                // If the user searched for something else while this was
                // loading, these results are old, so throw them away
                if (searchForThisRequest == activeSearch) {
                    // TMDB's popular list can shift between pages and repeat a
                    // movie, so skip any we already have (the grid needs unique ids)
                    val newMovies = result.content
                        .map { it.toMovie() }
                        .filter { newMovie -> movies.none { it.id == newMovie.id } }
                    movies.addAll(newMovies)
                    currentPage = result.page.number
                    totalPages = result.page.totalPages
                }
            } catch (e: Exception) {
                Log.e("MovieBoxExplore", "Couldn't load page $pageToLoad", e)
                loadError = "Couldn't load movies. Check your connection and try again."
            } finally {
                isLoading = false
            }
        }
    }

    // Clears everything and starts over at page 1 with a new search
    fun startSearch(newSearch: String) {
        activeSearch = newSearch.trim()
        movies.clear()
        currentPage = 0
        totalPages = 1
        loadError = null
        scope.launch { gridState.scrollToItem(0) }
    }

    // True when the user has scrolled within 6 posters of the bottom
    val nearBottom by remember {
        derivedStateOf {
            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= gridState.layoutInfo.totalItemsCount - 6
        }
    }

    // Load page 1 at the start (or after a new search), then keep loading
    // more whenever the user gets near the bottom. Stops on an error so it
    // doesn't retry forever; the Retry button starts it again.
    LaunchedEffect(nearBottom, isLoading, currentPage, loadError) {
        val needsFirstPage = currentPage == 0
        if ((needsFirstPage || nearBottom) && !isLoading && loadError == null) {
            loadNextPage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 56.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ScreenSidePadding),
            placeholder = { Text("Search movies by title...") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    // Clearing the box goes back to popular movies
                    IconButton(onClick = {
                        searchText = ""
                        startSearch("")
                    }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search")
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    startSearch(searchText)
                }
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            // First page is loading
            movies.isEmpty() && isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            // First page failed
            movies.isEmpty() && loadError != null -> {
                ErrorMessage(message = loadError!!, onRetry = { loadError = null })
            }

            // Loaded, but nothing matched the search
            movies.isEmpty() && currentPage > 0 -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No movies found",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Adaptive(minSize = 110.dp),
                    contentPadding = PaddingValues(
                        start = ScreenSidePadding,
                        end = ScreenSidePadding,
                        bottom = 24.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(movies, key = { movie -> movie.id }) { movie ->
                        PosterBlock(
                            movie = movie,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                focusManager.clearFocus()
                                onOpenMovie(movie.id)
                            }
                        )
                    }

                    // Bottom of the grid: spinner while the next page loads,
                    // or a retry message if it failed. Spans the full width.
                    if (isLoading) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    } else if (loadError != null) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            ErrorMessage(message = loadError!!, onRetry = { loadError = null })
                        }
                    }
                }
            }
        }
    }
}

// Error text with a Retry button. Clearing the error lets loading start again.
@Composable
private fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextButton(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    MovieBoxTheme {
        ExploreScreen()
    }
}