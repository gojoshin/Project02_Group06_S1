package com.example.moviebox.dto;

public record PasswordRegisterRequest(
        String email,
        String displayName,
        String password
) {}
