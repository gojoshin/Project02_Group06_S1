package com.example.moviebox.dto;

public record PageMetaDto(
        int size,
        int number,
        long totalElements,
        int totalPages
) {}
