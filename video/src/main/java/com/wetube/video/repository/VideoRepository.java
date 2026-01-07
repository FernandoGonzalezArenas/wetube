package com.wetube.video.repository;

import com.wetube.video.entity.VideoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<VideoEntity, Long> {
    @Query("SELECT v FROM VideoEntity v WHERE LOWER(v.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<VideoEntity> searchByTitle(String keyword, Pageable pageable);
    @Query("SELECT v FROM VideoEntity v WHERE (:lastId IS NULL OR v.id < :lastId) ORDER BY v.id DESC")
    List<VideoEntity> findNextVideos(Long lastId, Pageable pageable);

}
