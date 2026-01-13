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
    repository.save(new VideoEntity(null, 1L, "spring boot intro", "desc", "url", null));
repository.save(new VideoEntity(null, 2L, "advanced java", "desc", "url", null));

    Page<VideoEntity> result=repository.searchByTitle("spring", PageRequest.of(0, 10));
assertEquals(1, result.getTotalElements());
assertEquals("spring boot intro", result.getContent().get(0).getTitle());
}

@Test
    @DisplayName("debe retornar los siguientes videos basados en el ultimo ID (paginacion por cursor)")
    void shouldFindNextVideosBasedOnLastId(){
//guardando videos
    VideoEntity v1=repository.save(new VideoEntity(null, 101L, "video 1", "d", "u", "t"));
    VideoEntity v2=repository.save(new VideoEntity(null, 101L, "video 2", "d", "u", "t"));
    VideoEntity v3=repository.save(new VideoEntity(null, 101L, "video 3", "d", "u", "t"));

    Long lastId=v3.getId();

    //pedimos videos con id menor a v3
    Pageable pageable=PageRequest.of(0, 2);
    List<VideoEntity> result=repository.findNextVideos(lastId, pageable);

    //validaciones
    assertEquals(2, result.size());
    assertEquals(v1.getId(), result.get(1).getId());
}

}
