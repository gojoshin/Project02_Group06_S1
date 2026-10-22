package com.example.moviebox.controller;

import com.example.moviebox.dto.UserDto;
import com.example.moviebox.entity.Role;
import com.example.moviebox.entity.UserAccount;
import com.example.moviebox.repository.UserAccountRepository;
import com.example.moviebox.service.GoogleAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final GoogleAuthService googleAuthService;
    private final UserAccountRepository userRepository;

    public AuthController(GoogleAuthService googleAuthService, UserAccountRepository userRepository) {
        this.googleAuthService = googleAuthService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginWithGoogle(@RequestBody Map<String, String> request) {
        String idTokenString = request.get("idToken");
        if (idTokenString == null || idTokenString.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing idToken");
        }

        GoogleIdToken.Payload payload = googleAuthService.verifyToken(idTokenString);
        if (payload == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Google ID token");
        }

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        Optional<UserAccount> userOpt = userRepository.findByEmail(email);
        UserAccount user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            // Register new user (First user gets ADMIN for testing purposes)
            Role role = userRepository.count() == 0 ? Role.ADMIN : Role.USER;
            user = new UserAccount(email, name != null ? name : email, role);
            user = userRepository.save(user);
        }

        // Return user info. In a full implementation, you would also return a JWT token here.
        return ResponseEntity.ok(new UserDto(user.getId(), user.getEmail(), user.getDisplayName(), user.getRole()));
    }
}
