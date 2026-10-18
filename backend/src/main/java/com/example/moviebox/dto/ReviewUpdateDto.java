package com.example.moviebox.dto;

public record ReviewUpdateDto(
        Integer rating,
        String content,
        Boolean hasSpoilers
) {}
