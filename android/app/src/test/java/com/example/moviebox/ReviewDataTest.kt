package com.example.moviebox

import com.example.moviebox.data.MediaType
import com.example.moviebox.data.Review
import com.example.moviebox.data.sampleReviews
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReviewDataTest {

    @Test
    fun sampleReviews_isNotEmpty() {
        assertTrue("sampleReviews list should not be empty", sampleReviews.isNotEmpty())
    }

    @Test
    fun sampleReviews_hasValidFields() {
        sampleReviews.forEach { review ->
            assertTrue("Review ID should be positive", review.id > 0)
            assertTrue("Rating should be between 1 and 5", review.rating in 1..5)
            assertTrue("Review content should not be blank", review.content.isNotBlank())
        }
    }

    @Test
    fun review_dataClassCopyAndEquals() {
        val original = Review(
            id = 42,
            mediaId = 15,
            mediaType = MediaType.MOVIE,
            userId = 7,
            rating = 4,
            content = "Great movie!",
            createdAt = "2026-10-01T12:00:00Z",
            updatedAt = "2026-10-02T15:30:00Z"
        )
        val copy = original.copy(rating = 5, content = "Changed my mind, it's perfect.")

        assertEquals(42, copy.id)
        assertEquals(5, copy.rating)
        assertEquals("Changed my mind, it's perfect.", copy.content)
        assertEquals(15, copy.mediaId)
    }
}
