package com.example.moviebox.repository;

import com.example.moviebox.entity.FavoriteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {
    List<FavoriteItem> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
