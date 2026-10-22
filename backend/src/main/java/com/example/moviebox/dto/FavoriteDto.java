package com.example.moviebox.dto;

//Favorites on LandingScreen
public record FavoriteDto(
        Long id,
        String mediaType,
        Long mediaId
) {}