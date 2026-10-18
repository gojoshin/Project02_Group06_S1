package com.example.moviebox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviebox.data.MediaType
import com.example.moviebox.data.Review
import com.example.moviebox.ui.theme.MovieBoxTheme

@Composable
fun ReviewItem(
    review: Review,
    modifier: Modifier = Modifier
) {
    var isSpoilerRevealed by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Header: User ID and Rating
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "User ${review.userId}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "★ ${review.rating}/5",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Content: hidden if it has spoilers and hasn't been revealed
        if (review.hasSpoilers && !isSpoilerRevealed) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { isSpoilerRevealed = true }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "This review contains spoilers. Tap to reveal.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Text(
                text = review.content,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewItemPreview() {
    MovieBoxTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ReviewItem(
                review = Review(
                    id = 1,
                    mediaId = 1,
                    mediaType = MediaType.MOVIE,
                    userId = 1,
                    rating = 5,
                    content = "This is a great movie!",
                    hasSpoilers = false,
                    createdAt = "2024-03-05T12:00:00Z",
                    updatedAt = "2024-03-05T12:00:00Z"
                )
            )
            ReviewItem(
                review = Review(
                    id = 2,
                    mediaId = 1,
                    mediaType = MediaType.MOVIE,
                    userId = 2,
                    rating = 4,
                    content = "Dumbledore dies at the end.",
                    hasSpoilers = true,
                    createdAt = "2024-03-05T12:00:00Z",
                    updatedAt = "2024-03-05T12:00:00Z"
                )
            )
        }
    }
}
