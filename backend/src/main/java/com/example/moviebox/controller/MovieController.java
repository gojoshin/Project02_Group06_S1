package com.example.moviebox.controller;

import com.example.moviebox.dto.*;
import com.example.moviebox.dto.tmdb.TmdbMovie;
import com.example.moviebox.entity.MediaTypeEnum;
import com.example.moviebox.entity.Review;
import com.example.moviebox.repository.ReviewRepository;
import com.example.moviebox.dto.tmdb.TmdbPageResponse;
import com.example.moviebox.service.TmdbService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final TmdbService tmdbService;
    private final ReviewRepository reviewRepository;

    public MovieController(TmdbService tmdbService, ReviewRepository reviewRepository) {
        this.tmdbService = tmdbService;
        this.reviewRepository = reviewRepository;
    }

    /**
     * Every movie, one page at a time, for the Explore screen. Shows popular
     * movies by default, or search results when a query is given.
     */
    @Operation(summary = "Browse or search movies, 20 per page")
    @GetMapping
    public MoviePageDto getMovies(
            @Parameter(description = "Page number, starting at 1 (TMDB stops at 500)")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Optional title search, like \"dune\"")
            @RequestParam(required = false) String query
    ) {
        // TMDB only allows pages 1 through 500
        int safePage = Math.max(1, Math.min(page, 500));

        TmdbPageResponse<TmdbMovie> tmdbPage;
        if (query == null || query.isBlank()) {
            tmdbPage = tmdbService.getPopularMoviesPage(safePage);
        } else {
            tmdbPage = tmdbService.searchMovies(query.trim(), safePage);
        }

        List<TmdbMovie> movies = tmdbPage.results();
        int totalPages = tmdbPage.totalPages() != null ? Math.min(tmdbPage.totalPages(), 500) : 1;
        long totalResults = tmdbPage.totalResults() != null ? tmdbPage.totalResults() : movies.size();

        PageMetaDto pageMeta = new PageMetaDto(movies.size(), safePage, totalResults, totalPages);
        return new MoviePageDto(movies, pageMeta);
    }

    // Popular movies for the home screen, straight from TMDB.
    @GetMapping("/popular")
    public List<TmdbMovie> getPopularMovies() {
        return tmdbService.getPopularMovies();
    }

    // One movie's details from TMDB.
    @GetMapping("/{movieId}")
    public TmdbMovie getMovie(@PathVariable Long movieId) {
        return tmdbService.getMovie(movieId);
    }

    @GetMapping("/{movieId}/reviews")
    public ReviewPageDto getMovieReviews(
            @PathVariable Long movieId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<Review> reviews = reviewRepository.findByMediaIdAndMediaType(movieId, MediaTypeEnum.MOVIE);
        List<ReviewDto> dtos = reviews.stream()
                .map(r -> new ReviewDto(r.getId(), r.getMediaId(), r.getMediaType(),
                        r.getUserId(), r.getRating(), r.getContent(),r.getHasSpoilers(),
                        r.getCreatedAt(), r.getUpdatedAt()))
                .toList();

        PageMetaDto pageMeta = new PageMetaDto(size, page, dtos.size(), 1);
        return new ReviewPageDto(dtos, pageMeta);
    }

    @GetMapping("/{movieId}/streaming")
    public List<StreamingProviderDto> getMovieStreaming(@PathVariable Long movieId) {
        return tmdbService.getMovieStreamingProviders(movieId);
    }
}