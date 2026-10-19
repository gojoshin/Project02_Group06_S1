package com.example.moviebox.controller;

import com.example.moviebox.dto.HistoryItemCreateDto;
import com.example.moviebox.dto.HistoryItemDto;
import com.example.moviebox.entity.HistoryItem;
import com.example.moviebox.exception.ResourceNotFoundException;
import com.example.moviebox.repository.HistoryItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/history")
public class HistoryController {

    private final HistoryItemRepository historyItemRepository;

    public HistoryController(HistoryItemRepository historyItemRepository) {
        this.historyItemRepository = historyItemRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HistoryItemDto createHistoryItem(@RequestBody HistoryItemCreateDto createDto) {
        Long currentUserId = 1L; // Hardcoded auth simulation like other endpoints
        HistoryItem item = new HistoryItem(currentUserId, createDto.mediaType(), createDto.mediaId());
        historyItemRepository.save(item);
        return new HistoryItemDto(item.getId(), item.getUserId(), item.getMediaType(), item.getMediaId(), item.getWatchedAt());
    }

    @DeleteMapping("/{historyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHistoryItem(@PathVariable Long historyId) {
        if (!historyItemRepository.existsById(historyId)) {
            throw new ResourceNotFoundException("History item " + historyId + " not found");
        }
        historyItemRepository.deleteById(historyId);
    }
}
