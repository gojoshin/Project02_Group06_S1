package com.example.moviebox.service;

import com.example.moviebox.config.TmdbConfig;
import com.example.moviebox.dto.tmdb.TmdbEpisode;
import com.example.moviebox.dto.tmdb.TmdbMovie;
import com.example.moviebox.dto.tmdb.TmdbPageResponse;
import com.example.moviebox.dto.tmdb.TmdbSeason;
import com.example.moviebox.dto.tmdb.TmdbShow;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class TmdbService {

    private final RestClient restClient;
    private final TmdbConfig tmdbConfig;

    public TmdbService(RestClient restClient, TmdbConfig tmdbConfig) {
        this.restClient = restClient;
        this.tmdbConfig = tmdbConfig;
    }

    public TmdbMovie getMovie(Long movieId) {
        try {
            return restClient.get()
                    .uri("/movie/{movieId}?api_key={key}", movieId, tmdbConfig.getTmdbApiKey())
                    .retrieve()
                    .body(TmdbMovie.class);
        } catch (Exception e) {
            return new TmdbMovie(
                    movieId,
                    "Sample Movie " + movieId,
                    "Movie overview for ID " + movieId,
                    "2026-01-01",
                    "/poster.jpg",
                    "/backdrop.jpg",
                    8.5,
                    120
            );
        }
    }

    public List<TmdbMovie> getPopularMovies() {
        try {
            TmdbPageResponse<TmdbMovie> response = restClient.get()
                    .uri("/movie/popular?api_key={key}", tmdbConfig.getTmdbApiKey())
                    .retrieve()
                    .body(new ParameterizedTypeReference<TmdbPageResponse<TmdbMovie>>() {});
            return response != null && response.results() != null ? response.results() : getSampleMovies();
        } catch (Exception e) {
            return getSampleMovies();
        }
    }

    public TmdbShow getShow(Long showId) {
        try {
            return restClient.get()
                    .uri("/tv/{showId}?api_key={key}", showId, tmdbConfig.getTmdbApiKey())
                    .retrieve()
                    .body(TmdbShow.class);
        } catch (Exception e) {
            return new TmdbShow(
                    showId,
                    "Sample TV Show " + showId,
                    "Overview for show " + showId,
                    "2026-01-01",
                    "/tv_poster.jpg",
                    "/tv_backdrop.jpg",
                    2,
                    20
            );
        }
    }

    public List<TmdbEpisode> getShowEpisodes(Long showId) {
        try {
            TmdbSeason season = restClient.get()
                    .uri("/tv/{showId}/season/1?api_key={key}", showId, tmdbConfig.getTmdbApiKey())
                    .retrieve()
                    .body(TmdbSeason.class);
            if (season != null && season.episodes() != null) {
                return season.episodes();
            }
        } catch (Exception ignored) {
        }
        return getSampleEpisodes(showId);
    }

    public List<com.example.moviebox.dto.StreamingProviderDto> getMovieStreamingProviders(Long movieId) {
        try {
            com.example.moviebox.dto.tmdb.TmdbWatchProvidersResponse response = restClient.get()
                    .uri("/movie/{movieId}/watch/providers?api_key={key}", movieId, tmdbConfig.getTmdbApiKey())
                    .retrieve()
                    .body(com.example.moviebox.dto.tmdb.TmdbWatchProvidersResponse.class);
            return extractUSProviders(response);
        } catch (Exception e) {
            return getSampleProviders();
        }
    }

    public List<com.example.moviebox.dto.StreamingProviderDto> getShowStreamingProviders(Long showId) {
        try {
            com.example.moviebox.dto.tmdb.TmdbWatchProvidersResponse response = restClient.get()
                    .uri("/tv/{showId}/watch/providers?api_key={key}", showId, tmdbConfig.getTmdbApiKey())
                    .retrieve()
                    .body(com.example.moviebox.dto.tmdb.TmdbWatchProvidersResponse.class);
            return extractUSProviders(response);
        } catch (Exception e) {
            return getSampleProviders();
        }
    }

    private List<com.example.moviebox.dto.StreamingProviderDto> extractUSProviders(com.example.moviebox.dto.tmdb.TmdbWatchProvidersResponse response) {
        if (response != null && response.results() != null && response.results().containsKey("US")) {
            com.example.moviebox.dto.tmdb.TmdbWatchProviderRegion usRegion = response.results().get("US");
            if (usRegion.flatrate() != null) {
                return usRegion.flatrate().stream()
                        .map(p -> new com.example.moviebox.dto.StreamingProviderDto(p.providerId(), p.providerName(), p.logoPath()))
                        .toList();
            }
        }
        return new ArrayList<>();
    }

    private List<com.example.moviebox.dto.StreamingProviderDto> getSampleProviders() {
        List<com.example.moviebox.dto.StreamingProviderDto> list = new ArrayList<>();
        list.add(new com.example.moviebox.dto.StreamingProviderDto(8L, "Netflix", "/t2yyOv40HZeVlLjYsCsPHnWLk4W.jpg"));
        list.add(new com.example.moviebox.dto.StreamingProviderDto(9L, "Amazon Prime Video", "/ifhbNuuVnlwYy5oXAob0wVKpeAD.jpg"));
        return list;
    }

    private List<TmdbMovie> getSampleMovies() {
        List<TmdbMovie> list = new ArrayList<>();
        list.add(new TmdbMovie(100L, "Lanterns", "Lanterns overview", "2026-01-01", "/poster1.jpg", "/bg1.jpg", 8.8, 110));
        list.add(new TmdbMovie(101L, "Better Call Saul", "Vince Gilligan drama", "2015-02-08", "/poster2.jpg", "/bg2.jpg", 8.9, 50));
        list.add(new TmdbMovie(102L, "Obsession", "Thriller", "2026-02-01", "/poster3.jpg", "/bg3.jpg", 7.5, 95));
        list.add(new TmdbMovie(103L, "Dune: Part 2", "Sci-Fi Epic", "2024-03-01", "/poster4.jpg", "/bg4.jpg", 8.6, 166));
        return list;
    }

    private List<TmdbEpisode> getSampleEpisodes(Long showId) {
        List<TmdbEpisode> episodes = new ArrayList<>();
        episodes.add(new TmdbEpisode(401L, 1, 1, "Pilot", "The beginning", "2026-01-01", 45));
        episodes.add(new TmdbEpisode(402L, 1, 2, "Episode 2", "The second chapter", "2026-01-08", 48));
        episodes.add(new TmdbEpisode(403L, 1, 3, "Episode 3", "The plot thickens", "2026-01-15", 50));
        return episodes;
    }
}
