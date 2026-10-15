package com.example.moviebox.dto;

public record SeasonProgressDto(
        Integer seasonNumber,
        Integer totalEpisodes,
        Integer watchedEpisodes
) {}
