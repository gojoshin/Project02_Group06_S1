package com.example.moviebox.dto;

public record PasswordLoginRequest(
        String email,
        String password
) {}
