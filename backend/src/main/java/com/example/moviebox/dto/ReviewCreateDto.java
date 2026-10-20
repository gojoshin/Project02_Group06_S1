package com.example.moviebox.dto;

import com.example.moviebox.entity.MediaTypeEnum;

public record ReviewCreateDto(
        Long mediaId,
        MediaTypeEnum mediaType,
        Integer rating,
        String content,
        Boolean hasSpoilers
) {}
