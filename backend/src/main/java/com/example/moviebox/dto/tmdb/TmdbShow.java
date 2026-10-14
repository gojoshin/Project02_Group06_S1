package com.example.moviebox.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbShow(
        Long id,
        String name,
        String overview,
        @JsonProperty("first_air_date") String firstAirDate,
        @JsonProperty("poster_path") String posterPath,
        @JsonProperty("backdrop_path") String backdropPath,
        @JsonProperty("number_of_seasons") Integer numberOfSeasons,
        @JsonProperty("number_of_episodes") Integer numberOfEpisodes
) {}
