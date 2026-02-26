package com.wetube.video.repository;

import com.wetube.video.entity.VideoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class VideoRepositoryTest {

@Autowired
    private VideoRepository repository;

@Test
@DisplayName("debe buscar videos ignorando mayusculas o minusculas")
    void ShouldFindVideosByTitleCaseInsensitive(){
    repository.save(VideoEntity.builder()
            .userId(1L)
            .title("spring boot intro")
            .description("desc")
            .videoUrl("url")
            .build());
repository.save(VideoEntity.builder().userId(2L)
        .title("advanced java")
        .description("desc")
        .videoUrl("url")
        .build());

    Page<VideoEntity> result=repository.searchByTitle("spring", PageRequest.of(0, 10));
assertEquals(1, result.getTotalElements());
assertEquals("spring boot intro", result.getContent().get(0).getTitle());
}

@Test
    @DisplayName("debe retornar los siguientes videos basados en el ultimo ID (paginacion por cursor)")
    void shouldFindNextVideosBasedOnLastId(){
//guardando videos
    VideoEntity v1=repository.save(VideoEntity.builder()
            .userId(101L)
            .title("video 1")
            .description("d")
            .videoUrl("u")
            .thumbnailUrl("t")
            .build());
    VideoEntity v2=repository.save(VideoEntity.builder()
            .userId(101L)
            .title("video 2")
            .description("d")
            .videoUrl("u")
            .thumbnailUrl("t")
            .build());
    VideoEntity v3=repository.save(VideoEntity.builder()
            .userId(101L)
            .title("video 3")
            .description("d")
            .videoUrl("u")
            .thumbnailUrl("t")
            .build());

    Long lastId=v3.getId();

    //pedimos videos con id menor a v3
    Pageable pageable=PageRequest.of(0, 2);
    List<VideoEntity> result=repository.findNextVideos(lastId, pageable);

    //validaciones
    assertEquals(2, result.size());
    assertEquals(v1.getId(), result.get(1).getId());
}

}
