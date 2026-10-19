package com.example.moviebox.controller;

import com.example.moviebox.dto.*;
import com.example.moviebox.entity.UserAccount;
import com.example.moviebox.exception.ResourceNotFoundException;
import com.example.moviebox.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserAccountRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final EpisodeWatchStatusRepository episodeWatchStatusRepository;
    private final WatchlistItemRepository watchlistItemRepository;
    private final FavoriteItemRepository favoriteItemRepository;

    public UserController(
            UserAccountRepository userRepository,
            ReviewRepository reviewRepository,
            EpisodeWatchStatusRepository episodeWatchStatusRepository,
            WatchlistItemRepository watchlistItemRepository,
            FavoriteItemRepository favoriteItemRepository
    ) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.episodeWatchStatusRepository = episodeWatchStatusRepository;
        this.watchlistItemRepository = watchlistItemRepository;
        this.favoriteItemRepository = favoriteItemRepository;
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteMyAccount() {
        Long currentUserId = 1L;
        deleteUserData(currentUserId);
    }

    @GetMapping
    public UserPageDto listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<UserAccount> users = userRepository.findAll();
        List<UserDto> dtos = users.stream()
                .map(u -> new UserDto(u.getId(), u.getEmail(), u.getDisplayName(), u.getRole()))
                .toList();

        PageMetaDto pageMeta = new PageMetaDto(size, page, dtos.size(), 1);
        return new UserPageDto(dtos, pageMeta);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        return new UserDto(user.getId(), user.getEmail(), user.getDisplayName(), user.getRole());
    }

    @PatchMapping("/{userId}")
    public UserDto updateUserRole(@PathVariable Long userId, @RequestBody UserPatchDto patch) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));

        if (patch.role() != null) {
            user.setRole(patch.role());
            userRepository.save(user);
        }
        return new UserDto(user.getId(), user.getEmail(), user.getDisplayName(), user.getRole());
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteUser(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User " + userId + " not found");
        }
        deleteUserData(userId);
    }

    private void deleteUserData(Long userId) {
        reviewRepository.deleteByUserId(userId);
        episodeWatchStatusRepository.deleteByUserId(userId);
        watchlistItemRepository.deleteByUserId(userId);
        favoriteItemRepository.deleteByUserId(userId);
        userRepository.findById(userId).ifPresent(userRepository::delete);
    }
}