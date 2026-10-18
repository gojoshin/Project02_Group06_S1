package com.example.moviebox.dto.tmdb;

import java.util.Map;

public record TmdbWatchProvidersResponse(
        Long id,
        Map<String, TmdbWatchProviderRegion> results
) {}
