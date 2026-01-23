package com.wetube.likes.repository;

import com.wetube.likes.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
Optional<LikeEntity> findByUserIdAndVideoId(Long userId, Long videoId);
long countByVideoId(Long videoId);
boolean existsByUserIdAndVideoId(Long userId, Long videoId);
}
