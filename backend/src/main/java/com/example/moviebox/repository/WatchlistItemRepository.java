package com.example.moviebox.repository;

import com.example.moviebox.entity.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WatchlistItemRepository extends JpaRepository<WatchlistItem, Long> {
    List<WatchlistItem> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
