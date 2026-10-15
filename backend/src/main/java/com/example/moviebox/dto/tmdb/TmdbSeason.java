package com.example.moviebox.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record TmdbSeason(
        Long id,
        @JsonProperty("season_number") Integer seasonNumber,
        String name,
        @JsonProperty("episode_count") Integer episodeCount,
        List<TmdbEpisode> episodes
) {}
