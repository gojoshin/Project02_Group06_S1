package com.example.moviebox.dto;

import java.util.List;

public record ReviewPageDto(
        List<ReviewDto> content,
        PageMetaDto page
) {}
