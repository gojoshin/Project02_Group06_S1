package com.example.moviebox.controller;

import com.example.moviebox.dto.EpisodeDto;
import com.example.moviebox.dto.EpisodeStatusUpdateDto;
import com.example.moviebox.entity.EpisodeWatchStatus;
import com.example.moviebox.repository.EpisodeWatchStatusRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/episodes")
public class EpisodeController {

    private final EpisodeWatchStatusRepository repository;

    public EpisodeController(EpisodeWatchStatusRepository repository) {
        this.repository = repository;
    }

    @PutMapping("/{episodeId}/status")
    public EpisodeDto updateEpisodeStatus(
            @PathVariable Long episodeId,
            @RequestBody EpisodeStatusUpdateDto request
    ) {
        Long currentUserId = 1L;
        Long mockShowId = 100L;

        EpisodeWatchStatus status = repository.findByUserIdAndEpisodeId(currentUserId, episodeId)
                .orElseGet(() -> new EpisodeWatchStatus(currentUserId, mockShowId, episodeId, 1, 1, false));

        status.setWatched(request.watched());
        repository.save(status);

        return new EpisodeDto(
                episodeId,
                status.getShowId(),
                status.getSeasonNumber(),
                status.getEpisodeNumber(),
                "Episode " + episodeId,
                status.isWatched()
        );
    }
}
