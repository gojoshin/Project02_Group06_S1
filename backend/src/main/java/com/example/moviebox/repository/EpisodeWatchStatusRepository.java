package com.example.moviebox.repository;

import com.example.moviebox.entity.EpisodeWatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EpisodeWatchStatusRepository extends JpaRepository<EpisodeWatchStatus, Long> {
    Optional<EpisodeWatchStatus> findByUserIdAndEpisodeId(Long userId, Long episodeId);
    List<EpisodeWatchStatus> findByUserIdAndShowId(Long userId, Long showId);
    void deleteByUserId(Long userId);
}