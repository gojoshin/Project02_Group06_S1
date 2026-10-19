package com.example.moviebox.dto;

/**
 * What the app needs to show a movie in a list or poster row.
 *
 * @param id        TMDB movie id
 * @param title     movie title
 * @param year      release year, or null if TMDB doesn't have a date
 * @param overview  short description
 * @param posterUrl full image URL, or null if there's no poster
 * @param rating    TMDB average rating out of 10
 */
public record MovieSummaryDto(
        Long id,
        String title,
        Integer year,
        String overview,
        String posterUrl,
        Double rating
) {}
