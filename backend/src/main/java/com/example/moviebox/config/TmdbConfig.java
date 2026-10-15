package com.example.moviebox.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TmdbConfig {

    @Value("${tmdb.api.url:https://api.themoviedb.org/3}")
    private String tmdbBaseUrl;

    @Value("${tmdb.api.key:demo_key}")
    private String tmdbApiKey;

    @Value("${tmdb.api.token:}")
    private String tmdbBearerToken;

    @Bean
    public RestClient tmdbRestClient() {
        RestClient.Builder builder = RestClient.builder()
                .baseUrl(tmdbBaseUrl);

        if (tmdbBearerToken != null && !tmdbBearerToken.isBlank()) {
            builder.defaultHeader("Authorization", "Bearer " + tmdbBearerToken);
        }

        return builder.build();
    }

    public String getTmdbApiKey() {
        return tmdbApiKey;
    }
}
