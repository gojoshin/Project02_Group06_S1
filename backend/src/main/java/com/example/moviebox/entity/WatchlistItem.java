package com.example.moviebox.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "watchlist")
public class WatchlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String mediaType;

    @Column(nullable = false)
    private Long mediaId;

    public WatchlistItem() {}

    public WatchlistItem(Long userId, String mediaType, Long mediaId) {
        this.userId = userId;
        this.mediaType = mediaType;
        this.mediaId = mediaId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }
    public Long getMediaId() { return mediaId; }
    public void setMediaId(Long mediaId) { this.mediaId = mediaId; }
}