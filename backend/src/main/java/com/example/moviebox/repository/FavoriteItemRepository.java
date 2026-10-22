package com.example.moviebox.repository;

import com.example.moviebox.entity.FavoriteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {
    List<FavoriteItem> findByUserId(Long userId);
    void deleteByUserId(Long userId);

    // Used by FavoriteController to find and remove one favorite
    Optional<FavoriteItem> findByUserIdAndMediaTypeAndMediaId(Long userId, String mediaType, Long mediaId);
    void deleteByUserIdAndMediaTypeAndMediaId(Long userId, String mediaType, Long mediaId);
}
