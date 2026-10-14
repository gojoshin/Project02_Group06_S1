package com.example.moviebox.repository;

import com.example.moviebox.entity.MediaTypeEnum;
import com.example.moviebox.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMediaIdAndMediaType(Long mediaId, MediaTypeEnum mediaType);
    List<Review> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
