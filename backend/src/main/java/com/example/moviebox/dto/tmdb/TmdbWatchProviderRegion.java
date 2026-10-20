package com.example.moviebox.dto.tmdb;

import java.util.List;

public record TmdbWatchProviderRegion(
        String link,
        List<TmdbProvider> flatrate,
        List<TmdbProvider> buy,
        List<TmdbProvider> rent
) {}
