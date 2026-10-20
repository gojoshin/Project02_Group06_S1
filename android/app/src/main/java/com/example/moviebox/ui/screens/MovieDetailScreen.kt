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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: Int,
    onBack: () -> Unit
) {
    val movie = sampleMovies.find { it.id == movieId }

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
            if (movie == null) {
                Text("Movie not found")
                return@Scaffold
            }

            // Simple header block mimicking the poster color
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(movie.posterColor)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Poster Placeholder",
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = movie.title,
                fontFamily = Display,
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Text(
                text = "${movie.year} • Directed by ${movie.director}",
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
