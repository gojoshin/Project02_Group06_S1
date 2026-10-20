package com.example.moviebox.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long mediaId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaTypeEnum mediaType;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private boolean hasSpoilers = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Review() {}

    public Review(Long mediaId, MediaTypeEnum mediaType, Long userId, Integer rating, String content, boolean hasSpoilers) {
        this.mediaId = mediaId;
        this.mediaType = mediaType;
        this.userId = userId;
        this.rating = rating;
        this.content = content;
        this.hasSpoilers = hasSpoilers;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Review(Long mediaId, MediaTypeEnum mediaType, Long userId, Integer rating, String content) {
        this(mediaId, mediaType, userId, rating, content, false);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMediaId() { return mediaId; }
    public void setMediaId(Long mediaId) { this.mediaId = mediaId; }
    public MediaTypeEnum getMediaType() { return mediaType; }
    public void setMediaType(MediaTypeEnum mediaType) { this.mediaType = mediaType; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) {
        this.rating = rating;
        this.updatedAt = LocalDateTime.now();
    }
    public String getContent() { return content; }
    public void setContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
    public boolean getHasSpoilers() { return hasSpoilers; }
    public void setHasSpoilers(boolean hasSpoilers) {
        this.hasSpoilers = hasSpoilers;
        this.updatedAt = LocalDateTime.now();
    }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}