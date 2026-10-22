package com.example.moviebox.controller;

import com.example.moviebox.dto.FavoriteDto;
import com.example.moviebox.entity.FavoriteItem;
import com.example.moviebox.repository.FavoriteItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The signed-in user's favorites.
 * GET    /api/v1/favorites     *list of every favorite*
 * PUT    /api/v1/favorites/{mediaType}/{mediaId}   *add the moviue*
 * DELETE /api/v1/favorites/{mediaType}/{mediaId}   *remove the movie*
 */
@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteController {

    // Same stand-in as the other controllers until Google login hands us a real user
    private static final Long CURRENT_USER_ID = 1L;

    private final FavoriteItemRepository favoriteItemRepository;

    public FavoriteController(FavoriteItemRepository favoriteItemRepository) {
        this.favoriteItemRepository = favoriteItemRepository;
    }

    @GetMapping
    public List<FavoriteDto> getFavorites() {
        return favoriteItemRepository.findByUserId(CURRENT_USER_ID).stream()
                .map(FavoriteController::toDto)
                .toList();
    }

    // PUT so tapping the heart twice doesn't save the same movie twice
    @PutMapping("/{mediaType}/{mediaId}")
    public FavoriteDto addFavorite(@PathVariable String mediaType, @PathVariable Long mediaId) {
        String type = mediaType.toUpperCase();

        // If it's already saved, return that one. If not, just save a new one.
        FavoriteItem favorite = favoriteItemRepository
                .findByUserIdAndMediaTypeAndMediaId(CURRENT_USER_ID, type, mediaId)
                .orElseGet(() -> favoriteItemRepository.save(new FavoriteItem(CURRENT_USER_ID, type, mediaId)));

        return toDto(favorite);
    }

    @DeleteMapping("/{mediaType}/{mediaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void removeFavorite(@PathVariable String mediaType, @PathVariable Long mediaId) {
        favoriteItemRepository.deleteByUserIdAndMediaTypeAndMediaId(
                CURRENT_USER_ID, mediaType.toUpperCase(), mediaId);
    }

    private static FavoriteDto toDto(FavoriteItem item) {
        return new FavoriteDto(item.getId(), item.getMediaType(), item.getMediaId());
    }
}