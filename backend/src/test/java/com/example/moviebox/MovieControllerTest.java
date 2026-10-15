package com.example.moviebox;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPopularMovies_returnsMovieList() throws Exception {
        mockMvc.perform(get("/api/v1/movies/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").exists());
    }

    @Test
    void getMovie_returnsMovieDetails() throws Exception {
        mockMvc.perform(get("/api/v1/movies/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100));
    }

    @Test
    void getMovieReviews_returnsReviewsPage() throws Exception {
        mockMvc.perform(get("/api/v1/movies/100/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
