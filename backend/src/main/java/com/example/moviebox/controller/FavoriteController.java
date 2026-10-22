package com.example.moviebox.controller;

import com.example.moviebox.dto.FavoriteDto;
import com.example.moviebox.entity.FavoriteItem;
import com.example.moviebox.repository.FavoriteItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Each user's favorites. The app sends the logged in user's id in the URL.
 *
 * GET    /api/v1/users/{userId}/favorites        **every favorite**
 * PUT    /api/v1/users/{userId}/favorites/{mediaType}/{mediaId}   **add one**
 * DELETE /api/v1/users/{userId}/favorites/{mediaType}/{mediaId}   **remove one**
 */
@RestController
@RequestMapping("/api/v1/users/{userId}/favorites")
public class FavoriteController {

    private final FavoriteItemRepository favoriteItemRepository;

    public FavoriteController(FavoriteItemRepository favoriteItemRepository) {
        this.favoriteItemRepository = favoriteItemRepository;
    }

    @GetMapping
    public List<FavoriteDto> getFavorites(@PathVariable Long userId) {
        return favoriteItemRepository.findByUserId(userId).stream()
                .map(FavoriteController::toDto)
                .toList();
    }

    // Dont save the same movie twice
    @PutMapping("/{mediaType}/{mediaId}")
    public FavoriteDto addFavorite(
            @PathVariable Long userId,
            @PathVariable String mediaType,
            @PathVariable Long mediaId
    ) {
        String type = mediaType.toUpperCase();

        // If it's already saved, return that one.If not, save a new one.
        FavoriteItem favorite = favoriteItemRepository
                .findByUserIdAndMediaTypeAndMediaId(userId, type, mediaId)
                .orElseGet(() -> favoriteItemRepository.save(new FavoriteItem(userId, type, mediaId)));

        return toDto(favorite);
    }

    @DeleteMapping("/{mediaType}/{mediaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void removeFavorite(
            @PathVariable Long userId,
            @PathVariable String mediaType,
            @PathVariable Long mediaId
    ) {
        favoriteItemRepository.deleteByUserIdAndMediaTypeAndMediaId(userId, mediaType.toUpperCase(), mediaId);
    }

    private static FavoriteDto toDto(FavoriteItem item) {
        return new FavoriteDto(item.getId(), item.getMediaType(), item.getMediaId());
    }
}