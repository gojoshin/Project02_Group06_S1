
package com.example.moviebox

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviebox.data.FavoritesRetrofit
import com.example.moviebox.data.sampleMovies
import com.example.moviebox.ui.AuthState
import com.example.moviebox.ui.AuthViewModel
import com.example.moviebox.ui.components.ComingSoonScreen
import com.example.moviebox.ui.screens.AdminScreen
import com.example.moviebox.ui.screens.FavoritesScreen
import com.example.moviebox.ui.screens.HistoryScreen
import com.example.moviebox.ui.screens.LandingScreen
import com.example.moviebox.ui.screens.LoginScreen
import com.example.moviebox.ui.theme.MovieBoxTheme
import java.util.Locale
import kotlinx.coroutines.launch

// Screens for connecting the bottom bar UI are ui/screens/.

enum class Tab(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val requiresAdmin: Boolean = false
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    EXPLORE("Explore", Icons.Filled.Explore, Icons.Outlined.Explore),
    FAVORITES("Favorites", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    HISTORY("History", Icons.Filled.History, Icons.Outlined.History),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person),
    ADMIN("Admin", Icons.Filled.Person, Icons.Outlined.Person, true)
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )

        setContent {
            MovieBoxTheme {
                val authViewModel: AuthViewModel = viewModel()

                // Collect authentication state from ViewModel.
                val authState by authViewModel.authState
                    .collectAsStateWithLifecycle()

                when (authState) {
                    AuthState.LOGGED_OUT,
                    AuthState.ERROR,
                    AuthState.LOADING -> {
                        LoginScreen(
                            onGoogleTokenReceived = { token ->
                                authViewModel.authenticateWithGoogle(token)
                            }
                        )
                    }

                    AuthState.LOGGED_IN_USER -> {
                        MovieBoxApp(isAdmin = false)
                    }

                    AuthState.LOGGED_IN_ADMIN -> {
                        MovieBoxApp(isAdmin = true)
                    }
                }
            }
        }
    }

    /** The app shell. Whatever screen is active, plus the bottom bar. */
    @Composable
    fun MovieBoxApp(isAdmin: Boolean = false) {

        // Which tab is picked.
        var currentTab by rememberSaveable {
            mutableStateOf(Tab.HOME)
        }

        // Simple routing for detail screen.
        var currentMovieId by rememberSaveable {
            mutableStateOf<Int?>(null)
        }

        // FAVORITES
        // IDs of every movie the user hearted.
        // Loaded from the backend when the app opens.
        val favoriteMovieIds = remember {
            mutableStateListOf<Int>()
        }

        val favoritesScope = rememberCoroutineScope()

        // Load favorites from the backend.
        LaunchedEffect(Unit) {
            try {
                val savedFavorites = FavoritesRetrofit.api.getFavorites()

                favoriteMovieIds.clear()
                favoriteMovieIds.addAll(
                    savedFavorites.map { it.mediaId.toInt() }
                )
            } catch (e: Exception) {
                Log.e(
                    "MovieBoxFavorites",
                    "Couldn't load favorites. Is the backend running?",
                    e
                )
            }
        }

        // Adds the movie if it isn't a favorite yet,
        // or removes it if it is already favorited.
        val toggleFavorite: (Int) -> Unit = { movieId ->
            val wasFavorite = movieId in favoriteMovieIds

            // Update the screen immediately.
            if (wasFavorite) {
                favoriteMovieIds.remove(movieId)
            } else {
                favoriteMovieIds.add(movieId)
            }

            // Save the change to the backend.
            favoritesScope.launch {
                try {
                    if (wasFavorite) {
                        val response =
                            FavoritesRetrofit.api.removeFavorite(
                                "MOVIE",
                                movieId.toLong()
                            )

                        if (!response.isSuccessful) {
                            throw Exception(
                                "Delete failed: ${response.code()}"
                            )
                        }
                    } else {
                        FavoritesRetrofit.api.addFavorite(
                            "MOVIE",
                            movieId.toLong()
                        )
                    }
                } catch (e: Exception) {
                    Log.e(
                        "MovieBoxFavorites",
                        "Couldn't save favorite",
                        e
                    )

                    // Undo the UI change if saving failed.
                    if (wasFavorite) {
                        if (movieId !in favoriteMovieIds) {
                            favoriteMovieIds.add(movieId)
                        }
                    } else {
                        favoriteMovieIds.remove(movieId)
                    }
                }
            }
        }

        // Back button on any tab other than Home goes back
        // to Home instead of closing the app right away.
        BackHandler(
            enabled = currentTab != Tab.HOME || currentMovieId != null
        ) {
            if (currentMovieId != null) {
                currentMovieId = null
            } else {
                currentTab = Tab.HOME
            }
        }

        // Movie details screen.
        if (currentMovieId != null) {
            com.example.moviebox.ui.screens.MovieDetailScreen(
                movieId = currentMovieId!!,
                onBack = {
                    currentMovieId = null
                },
                isFavorite = currentMovieId!! in favoriteMovieIds,
                onToggleFavorite = {
                    toggleFavorite(currentMovieId!!)
                }
            )
            return
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                BottomTabBar(
                    currentTab = currentTab,
                    onSelectTab = { pickedTab ->
                        if (!pickedTab.requiresAdmin || isAdmin) {
                            currentTab = pickedTab
                        }
                    },
                    isAdmin = isAdmin
                )
            }
        ) { innerPadding ->

            // Keep screens from hiding behind the bottom bar.
            Box(Modifier.padding(innerPadding)) {
                when (currentTab) {

                    Tab.HOME -> LandingScreen(
                        onOpenMovie = { movieId ->
                            currentMovieId = movieId
                        }
                    )

                    Tab.EXPLORE -> ComingSoonScreen(
                        title = "Explore",
                        note = "Browse and search every movie. This will become the paginated list from the API."
                    )

                    // Restored Favorites screen.
                    Tab.FAVORITES -> FavoritesScreen(
                        favoriteMovies = sampleMovies.filter {
                            it.id in favoriteMovieIds
                        },
                        onOpenMovie = { movieId ->
                            currentMovieId = movieId
                        },
                        onRemoveFavorite = { movieId ->
                            toggleFavorite(movieId)
                        }
                    )

                    Tab.HISTORY -> HistoryScreen()

                    Tab.PROFILE -> ComingSoonScreen(
                        title = "Profile",
                        note = "Your ratings, sign out, delete account, and the admin link (admins only)."
                    )

                    Tab.ADMIN -> {
                        if (isAdmin) {
                            AdminScreen()
                        } else {
                            ComingSoonScreen(
                                title = "Admin",
                                note = "Admin access is required."
                            )
                        }
                    }
                }
            }
        }
    }

    // BottomTabBar
    //
    // Same look as last project's tab bar (thin line on top,
    // small uppercase labels, white for selected tab and gray
    // for the rest), but with an icon above each label.
    @Composable
    private fun BottomTabBar(
        currentTab: Tab,
        onSelectTab: (Tab) -> Unit,
        isAdmin: Boolean = false
    ) {
        val lineColor = MaterialTheme.colorScheme.outline
        val selectedColor = MaterialTheme.colorScheme.onBackground
        val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
                .background(MaterialTheme.colorScheme.surface)
                .drawBehind {
                    // Thin divider along the top of the bar.
                    drawLine(
                        lineColor,
                        Offset(0f, 0f),
                        Offset(size.width, 0f),
                        1.dp.toPx()
                    )
                }
                .navigationBarsPadding()
                .padding(vertical = 10.dp)
        ) {
            for (tab in Tab.entries) {

                // Only show the Admin tab to admins.
                if (tab.requiresAdmin && !isAdmin) {
                    continue
                }

                val isSelected = tab == currentTab
                val tabColor =
                    if (isSelected) selectedColor else unselectedColor

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .selectable(
                            selected = isSelected,
                            role = Role.Tab,
                            onClick = {
                                onSelectTab(tab)
                            }
                        )
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector =
                            if (isSelected) {
                                tab.selectedIcon
                            } else {
                                tab.unselectedIcon
                            },
                        contentDescription = null,
                        tint = tabColor,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = tab.label.uppercase(
                            Locale.getDefault()
                        ),
                        fontSize = 10.sp,
                        letterSpacing = 1.5.sp,
                        color = tabColor
                    )
                }
            }
        }
    }

    @Preview(showBackground = true, heightDp = 800)
    @Composable
    fun MovieBoxAppPreview() {
        MovieBoxTheme {
            MovieBoxApp()
        }
    }
}
