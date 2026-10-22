package com.example.moviebox.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviebox.data.User
import com.example.moviebox.ui.components.Hairline
import com.example.moviebox.ui.components.ScreenSidePadding
import com.example.moviebox.ui.components.ScreenTitle
import com.example.moviebox.ui.components.SectionLabel
import com.example.moviebox.ui.theme.Mono

@Composable
fun ProfileScreen(
    currentUser: User?,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 56.dp, bottom = 40.dp, start = ScreenSidePadding, end = ScreenSidePadding)
    ) {
        SectionLabel("Account")
        Spacer(Modifier.height(10.dp))
        ScreenTitle("Profile")
        Spacer(Modifier.height(30.dp))

        // User Account Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = currentUser?.displayName ?: "Signed In User",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = currentUser?.email ?: "user@example.com",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(6.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "ROLE: ${currentUser?.role ?: "USER"}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontFamily = Mono,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))
        Hairline()
        Spacer(Modifier.height(32.dp))

        // Sign Out Button
        Button(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Text(
                text = "SIGN OUT",
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}
