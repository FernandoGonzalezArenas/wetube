package com.wetube.comments.repository;

import com.wetube.comments.entity.CommentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByVideoId(Long videoId);
    List<CommentEntity> findByUsernameAuthor(String usernameAuthor);
@Query("SELECT c FROM CommentEntity c WHERE c.videoId = :videoId AND (:lastId IS NULL OR c.id < :lastId) ORDER BY c.id DESC")
    List<CommentEntity> findNextComments(Long videoId, Long lastId, Pageable pageable);

@Modifying
@Query("DELETE FROM CommentEntity c WHERE c.videoId = :videoId")
void deleteByVideoId(@Param("videoId") Long videoId);
}
