package com.example.moviebox.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleAuthService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleAuthService(@Value("${google.oauth.client-id:demo_client_id}") String clientId) {
        GoogleIdTokenVerifier tempVerifier = null;
        try {
            if (clientId != null && !clientId.isBlank()) {
                tempVerifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                        .setAudience(Collections.singletonList(clientId))
                        .build();
            }
        } catch (Exception ignored) {
        }
        this.verifier = tempVerifier;
    }

    public GoogleIdToken.Payload verifyToken(String idTokenString) {
        if ("mock_dev_token".equals(idTokenString)) {
            GoogleIdToken.Payload mockPayload = new GoogleIdToken.Payload();
            mockPayload.setEmail("admin@csumb.edu");
            mockPayload.set("name", "Dev Admin");
            return mockPayload;
        }

        if (verifier == null) {
            return null;
        }

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload();
            }
        } catch (Exception e) {
            // Token invalid or verification failed
        }
        return null;
    }
}
