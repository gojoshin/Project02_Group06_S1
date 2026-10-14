package com.example.moviebox.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbEpisode(
        Long id,
        @JsonProperty("season_number") Integer seasonNumber,
        @JsonProperty("episode_number") Integer episodeNumber,
        String name,
        String overview,
        @JsonProperty("air_date") String airDate,
        Integer runtime
) {}
