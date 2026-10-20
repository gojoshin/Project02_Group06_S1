package com.example.moviebox;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createAndUpdateReview_succeeds() throws Exception {
        String createJson = """
                {
                    "mediaId": 100,
                    "mediaType": "MOVIE",
                    "rating": 5,
                    "content": "Awesome movie!",
                    "hasSpoilers": true
                }
                """;

        String responseStr = mockMvc.perform(post("/api/v1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.hasSpoilers").value(true))
                .andExpect(jsonPath("$.content").value("Awesome movie!"))
                .andReturn().getResponse().getContentAsString();

        String updateJson = """
                {
                    "rating": 4,
                    "content": "Updated review content",
                    "hasSpoilers": false
                }
                """;

        mockMvc.perform(put("/api/v1/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.hasSpoilers").value(false))
                .andExpect(jsonPath("$.content").value("Updated review content"));
    }
}
