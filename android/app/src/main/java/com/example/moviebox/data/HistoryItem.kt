package com.example.moviebox.data

import java.time.LocalDate

data class HistoryItem(
    val id: Int,
    val movie: Movie,
    val watchedAt: LocalDate
)

val sampleHistory = listOf(
    HistoryItem(1, sampleMovies[1], LocalDate.now().minusDays(1)),
    HistoryItem(2, sampleMovies[3], LocalDate.now().minusDays(5)),
    HistoryItem(3, sampleMovies[5], LocalDate.now().minusDays(10))
)
