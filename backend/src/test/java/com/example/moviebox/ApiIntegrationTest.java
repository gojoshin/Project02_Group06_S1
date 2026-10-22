package com.example.moviebox;

import com.example.moviebox.dto.PasswordLoginRequest;
import com.example.moviebox.dto.PasswordRegisterRequest;
import com.example.moviebox.entity.Role;
import com.example.moviebox.entity.UserAccount;
import com.example.moviebox.repository.UserAccountRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserAccountRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        userRepository.deleteAll();
    }

    @Test
    void testHappyPath200() {
        // 200 Happy Path - Register User
        PasswordRegisterRequest request = new PasswordRegisterRequest("test@example.com", "Test User", "password");
        
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/v1/auth/register")
        .then()
            .statusCode(201)
            .body("email", equalTo("test@example.com"));
    }

    @Test
    void testBadRequest400() {
        // 400 Bad Request - Missing Password
        PasswordRegisterRequest request = new PasswordRegisterRequest("test2@example.com", "Test User", "");
        
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/v1/auth/register")
        .then()
            .statusCode(400);
    }

    @Test
    void testUnauthorized401() {
        // Create user
        userRepository.save(new UserAccount("auth@example.com", "Auth User", passwordEncoder.encode("secret"), Role.USER));
        
        // 401 Unauthorized - Bad Password
        PasswordLoginRequest request = new PasswordLoginRequest("auth@example.com", "wrongpassword");
        
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/v1/auth/login/password")
        .then()
            .statusCode(401);
    }

    @Test
    void testForbidden403() {
        // Admin access requires the first user to be ADMIN
        // So we create a USER role instead
        userRepository.save(new UserAccount("user@example.com", "User", passwordEncoder.encode("secret"), Role.USER));
        
        // 403 Forbidden - GET users
        given()
        .when()
            .get("/api/v1/users")
        .then()
            .statusCode(403);
    }

    @Test
    void testNotFound404() {
        // Create an admin so we bypass 403 check
        userRepository.save(new UserAccount("admin@example.com", "Admin", passwordEncoder.encode("secret"), Role.ADMIN));
        
        // 404 Not Found - Get Non-existent user
        given()
        .when()
            .get("/api/v1/users/99999")
        .then()
            .statusCode(404);
    }
}
