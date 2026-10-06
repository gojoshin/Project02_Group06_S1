package com.example.moviebox.data


// Movie and show data. Added some current shows and favorites so the landing page has something to show for testing.
// TODO (when the API is ready): delete sampleMovies and load the real list

data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val director: String,
    // No poster images yet, so each movie gets a placeholder color.
    // The project spec says to store image URLs (not images) in the database,
    // so later this becomes something like val posterUrl: String
    val posterColor: Long
)
// LIST OF CURRENT SHOWS/MOVIES AND PERSONAL FAVORITES. DELETE IN FUTURE FOR API/DATABASE STUFF.
val sampleMovies = listOf(
    Movie(1, "Lanterns", 2026, "James Gunn", 0xFF3D5A5B),
    Movie(2, "Better Call Saul", 2015, "Vince Gilligan", 0xFF780606 ),
    Movie(3, "Obsession", 2026, "Curry Barker", 0xFF5C3A4E),
    Movie(4, "Dune: Part 2", 2024, "Denis Villeneuve", 0xFF2E3B2C),
    Movie(5, "Backrooms", 2026, "Kane Parsons (20 years old)", 0xFF6B4A2E),
    Movie(6, "Parasite", 2019, "Bong Joon Ho", 0xFF33363D)
)
