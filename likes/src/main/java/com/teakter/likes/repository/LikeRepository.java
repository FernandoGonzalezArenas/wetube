package com.teakter.likes.repository;

import com.teakter.likes.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
Optional<LikeEntity> findByUserIdAndVideoId(Long userId, Long videoId);
long countByVideoId(Long videoId);
boolean existsByUserIdAndVideoId(Long userId, Long videoId);
List<LikeEntity> findByUserId(Long userId);

@Modifying
@Query("DELETE FROM LikeEntity l WHERE l.videoId = :videoId")
void deleteByVideoId(@Param("videoId") Long videoId);
void deleteByUserId(Long userId);

}
