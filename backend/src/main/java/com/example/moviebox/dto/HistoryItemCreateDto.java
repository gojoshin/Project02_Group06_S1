package com.example.moviebox.dto;

import com.example.moviebox.entity.MediaTypeEnum;

public record HistoryItemCreateDto(
        Long mediaId,
        MediaTypeEnum mediaType
) {}
