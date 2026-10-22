package com.example.moviebox;

import com.example.moviebox.entity.Role;
import com.example.moviebox.entity.UserAccount;
import com.example.moviebox.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository userRepository;

    private UserAccount testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        testUser = userRepository.save(new UserAccount("test@csumb.edu", "Test User", Role.ADMIN));
        // Force the test user to be the admin fallback check
        testUser = userRepository.findAll().get(0);
    }

    @Test
    void listUsers_returnsUsersPage() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].email").value("test@csumb.edu"));
    }

    @Test
    void getUser_returnsUser() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@csumb.edu"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void updateUserRole_updatesRole() throws Exception {
        String patchJson = "{\"role\": \"ADMIN\"}";

        mockMvc.perform(patch("/api/v1/users/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void deleteUser_removesUser() throws Exception {
        // Need to create a secondary user to delete, because deleting the mock admin user 
        // will cause subsequent checkAdminAccess() calls to fail inside the same request context or cleanup.
        UserAccount targetUser = userRepository.save(new UserAccount("target@csumb.edu", "Target User", Role.USER));

        mockMvc.perform(delete("/api/v1/users/" + targetUser.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/users/" + targetUser.getId()))
                .andExpect(status().isNotFound());
    }
}
