package com.example.moviebox.dto;

import java.util.List;

public record HistoryPageDto(
        List<HistoryItemDto> content,
        PageMetaDto page
) {}
