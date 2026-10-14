package com.example.moviebox.controller;

import com.example.moviebox.dto.ReviewCreateDto;
import com.example.moviebox.dto.ReviewDto;
import com.example.moviebox.dto.ReviewUpdateDto;
import com.example.moviebox.entity.Review;
import com.example.moviebox.exception.ResourceNotFoundException;
import com.example.moviebox.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
        Review review = new Review(
                createDto.mediaId(),
                createDto.mediaType(),
                currentUserId,
                createDto.rating(),
                createDto.content()
        );
        Review saved = reviewRepository.save(review);
        ReviewDto dto = new ReviewDto(saved.getId(), saved.getMediaId(), saved.getMediaType(), saved.getUserId(), saved.getRating(), saved.getContent(), saved.getCreatedAt(), saved.getUpdatedAt());

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

        Review saved = reviewRepository.save(review);
        return new ReviewDto(saved.getId(), saved.getMediaId(), saved.getMediaType(), saved.getUserId(), saved.getRating(), saved.getContent(), saved.getCreatedAt(), saved.getUpdatedAt());
    }
}
