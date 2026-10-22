package com.example.moviebox.controller;

import com.example.moviebox.dto.PasswordLoginRequest;
import com.example.moviebox.dto.PasswordRegisterRequest;
import com.example.moviebox.dto.UserDto;
import com.example.moviebox.entity.Role;
import com.example.moviebox.entity.UserAccount;
import com.example.moviebox.repository.UserAccountRepository;
import com.example.moviebox.service.GoogleAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public AuthController(GoogleAuthService googleAuthService, UserAccountRepository userRepository, PasswordEncoder passwordEncoder) {
        this.googleAuthService = googleAuthService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
            Role role = userRepository.count() == 0 ? Role.ADMIN : Role.USER;
            user = new UserAccount(email, name != null ? name : email, role);
            user = userRepository.save(user);
        }

        return ResponseEntity.ok(new UserDto(user.getId(), user.getEmail(), user.getDisplayName(), user.getRole()));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerWithPassword(@RequestBody PasswordRegisterRequest request) {
        if (request.email() == null || request.email().isBlank() || request.password() == null || request.password().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already registered");
        }

        Role role = userRepository.count() == 0 ? Role.ADMIN : Role.USER;
        String hashedPassword = passwordEncoder.encode(request.password());
        String displayName = (request.displayName() != null && !request.displayName().isBlank()) ? request.displayName() : request.email();

        UserAccount user = new UserAccount(request.email(), displayName, hashedPassword, role);
        user = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDto(user.getId(), user.getEmail(), user.getDisplayName(), user.getRole()));
    }

    @PostMapping("/login/password")
    public ResponseEntity<UserDto> loginWithPassword(@RequestBody PasswordLoginRequest request) {
        if (request.email() == null || request.email().isBlank() || request.password() == null || request.password().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
        }

        UserAccount user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (user.getPasswordHash() == null || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return ResponseEntity.ok(new UserDto(user.getId(), user.getEmail(), user.getDisplayName(), user.getRole()));
    }
}
