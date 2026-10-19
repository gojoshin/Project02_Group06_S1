package com.example.moviebox.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history_items")
public class HistoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaTypeEnum mediaType;

    @Column(nullable = false)
    private Long mediaId;

    @Column(nullable = false)
    private LocalDateTime watchedAt;

    public HistoryItem() {}

    public HistoryItem(Long userId, MediaTypeEnum mediaType, Long mediaId) {
        this.userId = userId;
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.watchedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public MediaTypeEnum getMediaType() { return mediaType; }
    public void setMediaType(MediaTypeEnum mediaType) { this.mediaType = mediaType; }
    public Long getMediaId() { return mediaId; }
    public void setMediaId(Long mediaId) { this.mediaId = mediaId; }
    public LocalDateTime getWatchedAt() { return watchedAt; }
    public void setWatchedAt(LocalDateTime watchedAt) { this.watchedAt = watchedAt; }
}
