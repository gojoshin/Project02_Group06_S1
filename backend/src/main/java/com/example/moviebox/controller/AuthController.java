
package com.example.moviebox.controller;

import com.example.moviebox.service.GoogleAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final GoogleAuthService googleAuthService;

    public AuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(
            @RequestBody Map<String, String> request
    ) {
        String idToken = request.get("idToken");

        if (idToken == null || idToken.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Missing ID token"));
        }

        try {
            GoogleIdToken.Payload payload =
                    googleAuthService.verifyToken(idToken);

            return ResponseEntity.ok(
                    Map.of(
                            "status", "verified",
                            "displayName",
                            String.valueOf(payload.get("name"))
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid Google token"));
        }
    }
}
