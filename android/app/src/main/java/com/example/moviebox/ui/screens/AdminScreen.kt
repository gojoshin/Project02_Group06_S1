package com.example.moviebox.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviebox.data.User
import com.example.moviebox.ui.AdminViewModel
import com.example.moviebox.ui.components.Hairline
import com.example.moviebox.ui.components.ScreenSidePadding
import com.example.moviebox.ui.components.ScreenTitle
import com.example.moviebox.ui.components.SectionLabel

@Composable
fun AdminScreen(
    adminViewModel: AdminViewModel = viewModel()
) {
    val users by adminViewModel.users.collectAsState()
    val isLoading by adminViewModel.isLoading.collectAsState()
    val error by adminViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        adminViewModel.fetchUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 56.dp, bottom = 40.dp)
    ) {
        Column(Modifier.padding(horizontal = ScreenSidePadding)) {
            SectionLabel("Admin Console")
            Spacer(Modifier.height(10.dp))
            ScreenTitle("Manage Users")
            Spacer(Modifier.height(20.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = ScreenSidePadding, vertical = 8.dp)
        ) {
            items(users) { user ->
                UserRow(
                    user = user,
                    onDelete = { adminViewModel.deleteUser(user.id) },
                    onPromote = { adminViewModel.promoteToAdmin(user.id) }
                )
                Hairline()
            }
        }
    }
}

@Composable
fun UserRow(user: User, onDelete: () -> Unit, onPromote: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.displayName,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )
            Text(
                text = "${user.email} • ${user.role}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
        
        Row {
            if (user.role != "ADMIN") {
                TextButton(onClick = onPromote) {
                    Text("MAKE ADMIN")
                }
            }
            TextButton(onClick = onDelete) {
                Text("DELETE", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
