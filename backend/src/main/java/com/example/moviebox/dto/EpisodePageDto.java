package com.example.moviebox.dto;

import java.util.List;

public record EpisodePageDto(
        List<EpisodeDto> content,
        PageMetaDto page
) {}
