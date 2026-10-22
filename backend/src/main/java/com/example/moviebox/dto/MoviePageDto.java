package com.example.moviebox.dto;

import com.example.moviebox.dto.tmdb.TmdbMovie;

import java.util.List;

/**
 * One page of movies for the Explore screen.
 *
 * @param content the movies on this page (TMDB sends 20 at a time)
 * @param page    which page this is and how many pages there are in total
 */
public record MoviePageDto(
        List<TmdbMovie> content,
        PageMetaDto page
) {}