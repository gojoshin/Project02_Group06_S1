package com.example.moviebox.dto;

import java.util.List;

public record UserPageDto(
        List<UserDto> content,
        PageMetaDto page
) {}
