package com.example.moviebox.controller;

import com.example.moviebox.dto.*;
import com.example.moviebox.dto.tmdb.TmdbEpisode;
import com.example.moviebox.entity.EpisodeWatchStatus;
import com.example.moviebox.repository.EpisodeWatchStatusRepository;
import com.example.moviebox.service.TmdbService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shows")
public class ShowController {

    private final TmdbService tmdbService;
    private final EpisodeWatchStatusRepository episodeWatchStatusRepository;

    public ShowController(TmdbService tmdbService, EpisodeWatchStatusRepository episodeWatchStatusRepository) {
        this.tmdbService = tmdbService;
        this.episodeWatchStatusRepository = episodeWatchStatusRepository;
    }

    @GetMapping("/{showId}/episodes")
    public EpisodePageDto getShowEpisodes(
            @PathVariable Long showId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Long currentUserId = 1L;
        List<TmdbEpisode> tmdbEpisodes = tmdbService.getShowEpisodes(showId);
        List<EpisodeWatchStatus> watchedList = episodeWatchStatusRepository.findByUserIdAndShowId(currentUserId, showId);

        List<EpisodeDto> dtos = tmdbEpisodes.stream().map(ep -> {
            boolean isWatched = watchedList.stream()
                    .anyMatch(w -> w.getEpisodeId().equals(ep.id()) && w.isWatched());
            return new EpisodeDto(ep.id(), showId, ep.seasonNumber(), ep.episodeNumber(), ep.name(), isWatched);
        }).toList();

        PageMetaDto pageMeta = new PageMetaDto(size, page, dtos.size(), 1);
        return new EpisodePageDto(dtos, pageMeta);
    }

    @GetMapping("/{showId}/progress")
    public ShowProgressDto getShowProgress(@PathVariable Long showId) {
        Long currentUserId = 1L;
        List<TmdbEpisode> tmdbEpisodes = tmdbService.getShowEpisodes(showId);
        List<EpisodeWatchStatus> watchedList = episodeWatchStatusRepository.findByUserIdAndShowId(currentUserId, showId);

        int totalEpisodes = tmdbEpisodes.size();
        int watchedCount = (int) watchedList.stream().filter(EpisodeWatchStatus::isWatched).count();

        SeasonProgressDto season1 = new SeasonProgressDto(1, totalEpisodes, watchedCount);
        return new ShowProgressDto(showId, totalEpisodes, watchedCount, List.of(season1));
    }

    @GetMapping("/{showId}/streaming")
    public List<StreamingProviderDto> getShowStreaming(@PathVariable Long showId) {
        return tmdbService.getShowStreamingProviders(showId);
    }
}