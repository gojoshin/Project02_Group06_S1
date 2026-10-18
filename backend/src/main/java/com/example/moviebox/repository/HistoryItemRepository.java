package com.example.moviebox.repository;

import com.example.moviebox.entity.HistoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
    List<HistoryItem> findByUserIdOrderByWatchedAtDesc(Long userId);
    void deleteByUserId(Long userId);
}
