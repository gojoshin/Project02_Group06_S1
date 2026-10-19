package com.example.moviebox.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbProvider(
        @JsonProperty("provider_id") Long providerId,
        @JsonProperty("provider_name") String providerName,
        @JsonProperty("logo_path") String logoPath
) {}
