package com.example.moviebox.dto;

import java.util.List;

public record ShowProgressDto(
        Long showId,
        Integer totalEpisodes,
        Integer watchedEpisodes,
        List<SeasonProgressDto> seasons
) {}
