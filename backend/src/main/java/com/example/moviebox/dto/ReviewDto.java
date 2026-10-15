package com.example.moviebox.dto;

import com.example.moviebox.entity.MediaTypeEnum;
import java.time.LocalDateTime;

public record ReviewDto(
        Long id,
        Long mediaId,
        MediaTypeEnum mediaType,
        Long userId,
        Integer rating,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
