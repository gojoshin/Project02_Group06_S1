package com.example.moviebox;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EpisodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void updateEpisodeStatus_marksEpisodeAsWatched() throws Exception {
        String requestJson = "{\"watched\": true}";

        mockMvc.perform(put("/api/v1/episodes/404/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(404))
                .andExpect(jsonPath("$.watched").value(true));
    }
}
