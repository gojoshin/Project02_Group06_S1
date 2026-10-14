package com.example.moviebox.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbMovie(
        Long id,
        String title,
        String overview,
        @JsonProperty("release_date") String releaseDate,
        @JsonProperty("poster_path") String posterPath,
        @JsonProperty("backdrop_path") String backdropPath,
        @JsonProperty("vote_average") Double voteAverage,
        Integer runtime
) {}
