package com.example.moviebox

import com.example.moviebox.data.Movie
import com.example.moviebox.data.sampleMovies
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MovieDataTest {

    @Test
    fun sampleMovies_isNotEmpty() {
        assertTrue("sampleMovies list should not be empty", sampleMovies.isNotEmpty())
    }

    @Test
    fun sampleMovies_hasUniqueIds() {
        val ids = sampleMovies.map { it.id }
        assertEquals("All movies should have unique IDs", ids.size, ids.toSet().size)
    }

    @Test
    fun sampleMovies_hasValidFields() {
        sampleMovies.forEach { movie ->
            assertTrue("Movie ID should be positive", movie.id > 0)
            assertTrue("Movie title should not be blank", movie.title.isNotBlank())
            assertTrue("Movie director should not be blank", movie.director.isNotBlank())
            assertTrue("Movie year should be reasonable", movie.year > 1900)
        }
    }

    @Test
    fun movie_sortingByYearDescending_worksCorrectly() {
        val sorted = sampleMovies.sortedByDescending { it.year }.take(4)
        assertEquals(4, sorted.size)

        // Verify descending order by year
        for (i in 0 until sorted.size - 1) {
            assertTrue(
                "Movie at index $i (${sorted[i].year}) should be >= movie at ${i + 1} (${sorted[i + 1].year})",
                sorted[i].year >= sorted[i + 1].year
            )
        }
    }

    @Test
    fun movie_dataClassCopyAndEquals() {
        val original = Movie(100, "Test Movie", 2026, "Test Director", 0xFF000000)
        val copy = original.copy(title = "Updated Title")

        assertEquals(100, copy.id)
        assertEquals("Updated Title", copy.title)
        assertEquals(2026, copy.year)
        assertEquals("Test Director", copy.director)
        assertEquals(0xFF000000, copy.posterColor)
    }
}
