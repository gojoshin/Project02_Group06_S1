package com.example.moviebox.dto;

import com.example.moviebox.entity.MediaTypeEnum;
import java.time.LocalDateTime;

public record HistoryItemDto(
        Long id,
        Long userId,
        MediaTypeEnum mediaType,
        Long mediaId,
        LocalDateTime watchedAt
) {}
