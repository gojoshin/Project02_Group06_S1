package com.example.moviebox

import android.os.Bundle
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.moviebox.ui.components.ComingSoonScreen
import com.example.moviebox.ui.screens.LandingScreen
import com.example.moviebox.ui.theme.MovieBoxTheme
import java.util.Locale

// Screens for connecting the bottom bar UI are ui/screens/.

enum class Tab(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    EXPLORE("Explore", Icons.Filled.Explore, Icons.Outlined.Explore),
    FAVORITES("Favorites", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //DarkMode by default so makes the UI light and bright
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        setContent {
            MovieBoxTheme {
                // TODO (Sprint 2/3): Google sign-in goes here, before the app shows
                // and only show MovieBoxApp() once we have a token.
                MovieBoxApp()
            }
        }
    }
}

/** The app shell. Whatever screen is active, plus the bottom bar. */
@Composable
fun MovieBoxApp() {
    // Which tab is picked.
    var currentTab by rememberSaveable { mutableStateOf(Tab.HOME) }

    // Back button on any tab other than Home goes back to Home instead of closing the app right away
    BackHandler(enabled = currentTab != Tab.HOME) {
        currentTab = Tab.HOME
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomTabBar(
                currentTab = currentTab,
                onSelectTab = { pickedTab -> currentTab = pickedTab }
            )
        }
    ) { innerPadding ->
        // innerPadding to keep screens from hiding behind the bottom bar UI
        Box(Modifier.padding(innerPadding)) {
            when (currentTab) {
                Tab.HOME -> LandingScreen(
                    onOpenMovie = { movieId ->
                        // TODO: open the movie detail screen for movieId
                    }
                )

                Tab.EXPLORE -> ComingSoonScreen(
                    title = "Explore",
                    note = "Browse and search every movie. This will become the paginated list from the API."
                )

                Tab.FAVORITES -> ComingSoonScreen(
                    title = "Favorites",
                    note = "Movies you hearted will show up here."
                )

                Tab.PROFILE -> ComingSoonScreen(
                    title = "Profile",
                    note = "Your ratings, sign out, delete account, and the admin link (admins only)."
                )
            }
        }
    }
}


// BottomTabBar
//
// Same look as last project's tab bar (thin line on top, small uppercase
// labels, white for the selected tab and gray for the rest), but with an
// icon above each label instead of a dot.
@Composable
private fun BottomTabBar(currentTab: Tab, onSelectTab: (Tab) -> Unit) {
    val lineColor = MaterialTheme.colorScheme.outline
    val selectedColor = MaterialTheme.colorScheme.onBackground
    val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup()
            .background(MaterialTheme.colorScheme.surface)
            .drawBehind {
                // Thin divider along the top of the bar
                drawLine(lineColor, Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx())
            }
            .navigationBarsPadding()
            .padding(vertical = 10.dp)
    ) {
        for (tab in Tab.entries) {
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
