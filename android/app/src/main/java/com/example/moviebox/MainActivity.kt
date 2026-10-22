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
import androidx.compose.runtime.getValue
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
import com.example.moviebox.data.AuthRetrofit
import com.example.moviebox.data.LoginRequest
import com.example.moviebox.ui.AuthState
import com.example.moviebox.ui.AuthViewModel
import com.example.moviebox.ui.components.ComingSoonScreen
import com.example.moviebox.ui.screens.AdminScreen
import com.example.moviebox.ui.screens.HistoryScreen
import com.example.moviebox.ui.screens.LandingScreen
import com.example.moviebox.ui.screens.LoginScreen
import com.example.moviebox.ui.screens.ProfileScreen
import com.example.moviebox.ui.theme.MovieBoxTheme
import java.util.Locale

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
    ADMIN("Admin", Icons.Filled.Person, Icons.Outlined.Person, true) // Hidden by default
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        setContent {
            MovieBoxTheme {
                val authViewModel: AuthViewModel = viewModel()
                
                val authState by authViewModel.authState.collectAsStateWithLifecycle()
                val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
                val errorMessage by authViewModel.errorMessage.collectAsStateWithLifecycle()
                
                when (authState) {
                    AuthState.LOGGED_OUT, AuthState.ERROR, AuthState.LOADING -> {
                        LoginScreen(
                            onGoogleTokenReceived = { token ->
                                authViewModel.authenticateWithGoogle(token)
                            },
                            onPasswordLogin = { email, password ->
                                authViewModel.loginWithPassword(email, password)
                            },
                            onPasswordRegister = { email, displayName, password ->
                                authViewModel.registerWithPassword(email, displayName, password)
                            },
                            externalErrorMessage = errorMessage
                        )
                    }
                    AuthState.LOGGED_IN_USER -> {
                        MovieBoxApp(
                            isAdmin = false,
                            currentUser = currentUser,
                            onSignOut = { authViewModel.logout() }
                        )
                    }
                    AuthState.LOGGED_IN_ADMIN -> {
                        MovieBoxApp(
                            isAdmin = true,
                            currentUser = currentUser,
                            onSignOut = { authViewModel.logout() }
                        )
                    }
                }
            }
        }
    }
}

/** The app shell. Whatever screen is active, plus the bottom bar. */
@Composable
fun MovieBoxApp(
    isAdmin: Boolean = false,
    currentUser: com.example.moviebox.data.User? = null,
    onSignOut: () -> Unit = {}
) {
    // Which tab is picked.
    var currentTab by rememberSaveable { mutableStateOf(Tab.HOME) }

    // Simple routing for detail screen
    var currentMovieId by rememberSaveable { mutableStateOf<Int?>(null) }

    // Back button on any tab other than Home goes back to Home instead of closing the app right away
    BackHandler(enabled = currentTab != Tab.HOME || currentMovieId != null) {
        if (currentMovieId != null) {
            currentMovieId = null
        } else {
            currentTab = Tab.HOME
        }
    }

    if (currentMovieId != null) {
        com.example.moviebox.ui.screens.MovieDetailScreen(
            movieId = currentMovieId!!,
            onBack = { currentMovieId = null }
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomTabBar(
                currentTab = currentTab,
                isAdmin = isAdmin,
                onSelectTab = { pickedTab -> currentTab = pickedTab }
            )
        }
    ) { innerPadding ->
        // innerPadding to keep screens from hiding behind the bottom bar UI
        Box(Modifier.padding(innerPadding)) {
            when (currentTab) {
                Tab.HOME -> LandingScreen(
                    onOpenMovie = { movieId ->
                        currentMovieId = movieId
                    }
                )

                Tab.EXPLORE -> com.example.moviebox.ui.screens.ExploreScreen(
                    onOpenMovie = { movieId ->
                        currentMovieId = movieId
                    }
                )

                Tab.FAVORITES -> ComingSoonScreen(
                    title = "Favorites",
                    note = "Movies you hearted will show up here."
                )

                Tab.HISTORY -> HistoryScreen()

                Tab.PROFILE -> ProfileScreen(
                    currentUser = currentUser,
                    onSignOut = onSignOut
                )
                
                Tab.ADMIN -> AdminScreen()
            }
        }
    }
}


// BottomTabBar
@Composable
private fun BottomTabBar(currentTab: Tab, isAdmin: Boolean, onSelectTab: (Tab) -> Unit) {
    val lineColor = MaterialTheme.colorScheme.outline
    val selectedColor = MaterialTheme.colorScheme.onBackground
    val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
    
    val visibleTabs = Tab.entries.filter { !it.requiresAdmin || isAdmin }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup()
            .background(MaterialTheme.colorScheme.surface)
            .drawBehind {
                drawLine(lineColor, Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx())
            }
            .navigationBarsPadding()
            .padding(vertical = 10.dp)
    ) {
        for (tab in visibleTabs) {
            val isSelected = tab == currentTab
            val tabColor = if (isSelected) selectedColor else unselectedColor

            Column(
                modifier = Modifier
                    .weight(1f)
                    .selectable(selected = isSelected, role = Role.Tab, onClick = { onSelectTab(tab) })
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                    contentDescription = null, // label underneath covers it
                    tint = tabColor,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = tab.label.uppercase(Locale.getDefault()),
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
    MovieBoxTheme { MovieBoxApp() }
}
