package com.example.moviebox.dto;

/**
 * Everything the movie detail screen shows.
 *
 * @param id          TMDB movie id
 * @param title       movie title
 * @param year        release year, or null if unknown
 * @param overview    full description
 * @param posterUrl   full poster URL, or null
 * @param backdropUrl wide background image URL, or null
 * @param rating      TMDB average rating out of 10
 * @param runtime     length in minutes, or null
 * @param director    director's name, or null if TMDB doesn't list one
 */
public record MovieDetailDto(
        Long id,
        String title,
        Integer year,
        String overview,
        String posterUrl,
        String backdropUrl,
        Double rating,
        Integer runtime,
        String director
) {}
