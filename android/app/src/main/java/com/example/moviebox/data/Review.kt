package com.example.moviebox.data

data class Review(
    val id: Int,
    val mediaId: Int, // Refers to a Movie or Show ID
    val mediaType: MediaType,
    val userId: Int,
    val rating: Int,
    val content: String,
    val hasSpoilers: Boolean = false,
    val createdAt: String,
    val updatedAt: String
)

enum class MediaType {
    MOVIE, SHOW
}

// Sample reviews for testing before connecting to the API/database
val sampleReviews = listOf(
    Review(
        id = 1,
        mediaId = 4, // Dune: Part 2
        mediaType = MediaType.MOVIE,
        userId = 7,
        rating = 5,
        content = "Absolutely breathtaking visuals and score.",
        createdAt = "2024-03-05T12:00:00Z",
        updatedAt = "2024-03-05T12:00:00Z"
    ),
    Review(
        id = 2,
        mediaId = 2, // Better Call Saul
        mediaType = MediaType.SHOW,
        userId = 7,
        rating = 5,
        content = "One of the best prequels ever made.",
        createdAt = "2023-01-15T12:00:00Z",
        updatedAt = "2023-01-16T12:00:00Z"
    ),
    Review(
        id = 3,
        mediaId = 6, // Parasite
        mediaType = MediaType.MOVIE,
        userId = 7,
        rating = 5,
        content = "A masterpiece of modern cinema.",
        createdAt = "2020-02-10T12:00:00Z",
        updatedAt = "2020-02-10T12:00:00Z"
    )
)
