package com.example.moviebox.controller;

import com.example.moviebox.dto.*;
import com.example.moviebox.entity.Review;
import com.example.moviebox.exception.ResourceNotFoundException;
import com.example.moviebox.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewCreateDto createDto) {
        Long currentUserId = 1L;
        boolean spoilers = createDto.hasSpoilers() != null ? createDto.hasSpoilers() : false;
        Review review = new Review(
                createDto.mediaId(),
                createDto.mediaType(),
                currentUserId,
                createDto.rating(),
                createDto.content(),
                spoilers
        );
        Review saved = reviewRepository.save(review);
        ReviewDto dto = new ReviewDto(saved.getId(), saved.getMediaId(), saved.getMediaType(), saved.getUserId(), saved.getRating(), saved.getContent(), saved.getHasSpoilers(), saved.getCreatedAt(), saved.getUpdatedAt());

        return ResponseEntity.created(URI.create("/api/v1/reviews/" + saved.getId())).body(dto);
    }

    @PutMapping("/{reviewId}")
    public ReviewDto updateReview(@PathVariable Long reviewId, @RequestBody ReviewUpdateDto updateDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review " + reviewId + " not found"));

        if (updateDto.rating() != null) {
            review.setRating(updateDto.rating());
        }
        if (updateDto.content() != null) {
            review.setContent(updateDto.content());
        }
        if (updateDto.hasSpoilers() != null) {
            review.setHasSpoilers(updateDto.hasSpoilers());
        }

        Review saved = reviewRepository.save(review);
        return new ReviewDto(saved.getId(), saved.getMediaId(), saved.getMediaType(), saved.getUserId(), saved.getRating(), saved.getContent(), saved.getHasSpoilers(), saved.getCreatedAt(), saved.getUpdatedAt());
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByMovieId(@PathVariable Long movieId) {
        List<Review> reviews = reviewRepository.findByMediaIdAndMediaType(movieId, com.example.moviebox.entity.MediaTypeEnum.MOVIE);
        List<ReviewDto> dtos = reviews.stream()
                .map(saved -> new ReviewDto(saved.getId(), saved.getMediaId(), saved.getMediaType(), saved.getUserId(), saved.getRating(), saved.getContent(), saved.getHasSpoilers(), saved.getCreatedAt(), saved.getUpdatedAt()))
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}