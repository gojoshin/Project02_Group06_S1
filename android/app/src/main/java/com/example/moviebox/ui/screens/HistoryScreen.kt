package com.example.moviebox.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviebox.data.HistoryItem
import com.example.moviebox.data.sampleHistory
import com.example.moviebox.ui.components.Hairline
import com.example.moviebox.ui.components.ScreenSidePadding
import com.example.moviebox.ui.components.SectionLabel
import com.example.moviebox.ui.theme.Display
import com.example.moviebox.ui.theme.Mono
import com.example.moviebox.ui.theme.MovieBoxTheme
import java.time.format.DateTimeFormatter

@Composable
fun HistoryScreen(
    historyItems: List<HistoryItem> = sampleHistory
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 56.dp, bottom = 40.dp)
    ) {
        Column(Modifier.padding(horizontal = ScreenSidePadding)) {
            Text(
                text = "Watch History",
                fontFamily = Display,
                fontSize = 46.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Remember what you have watched and when.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(36.dp))
            SectionLabel("Your History")
            Spacer(Modifier.height(8.dp))
            Hairline()
        }

        LazyColumn(
            modifier = Modifier.padding(horizontal = ScreenSidePadding)
        ) {
            items(historyItems) { item ->
                HistoryListRow(item)
                Hairline()
            }
        }
    }
}

@Composable
private fun HistoryListRow(item: HistoryItem) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = item.movie.title,
                fontFamily = Display,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = item.movie.director,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = item.watchedAt.format(dateFormatter),
            fontFamily = Mono,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    MovieBoxTheme {
        HistoryScreen()
    }
}
