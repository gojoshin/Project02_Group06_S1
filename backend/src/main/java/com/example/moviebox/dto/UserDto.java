package com.example.moviebox.dto;

import com.example.moviebox.entity.Role;

public record UserDto(
        Long id,
        String email,
        String displayName,
        Role role
) {}
