package com.example.moviebox.dto;

public record EpisodeDto(
        Long id,
        Long showId,
        Integer seasonNumber,
        Integer episodeNumber,
        String title,
        boolean watched
) {}
