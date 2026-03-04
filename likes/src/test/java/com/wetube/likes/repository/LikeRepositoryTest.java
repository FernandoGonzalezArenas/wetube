package com.wetube.likes.repository;

import com.wetube.likes.entity.LikeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LikeRepositoryTest {

@Autowired
    private LikeRepository repository;

@Test
    void shouldSaveLikeSuccessfully(){
    LikeEntity like=LikeEntity.builder()
            .userId(1L)
            .videoId(100L)
            .build();

    LikeEntity saved=repository.save(like);

    //validaciones
    assertNotNull(saved.getId());
    assertTrue(repository.existsByUserIdAndVideoId(1L, 100L));
}

@Test
    void shouldFailWhenDuplicateLikeIsInserted(){
    LikeEntity like1=LikeEntity.builder()
            .userId(1L).videoId(100L).build();
    repository.saveAndFlush(like1);

    //intentamos guardar otra vez exactamente el mismo like
    LikeEntity like2=LikeEntity.builder()
            .userId(1L).videoId(100L).build();

    //debe lanzar una excepcion de integridad de datos al querer guardar el mismo like otra vez
    assertThrows(DataIntegrityViolationException.class, () -> {
        repository.saveAndFlush(like2);
    });
}

@Test
    void ShouldCountLikesByVideoId(){
    //simular likes en videos
    repository.save(LikeEntity.builder().userId(1L).videoId(200L).build());
    repository.save(LikeEntity.builder().userId(2L).videoId(200L).build());

    Long count=repository.countByVideoId(200L);

    //validacion
    assertEquals(2, count);
}

@Test
    void shouldFindAllLikesByUserId(){
    repository.save(LikeEntity.builder().userId(1L).videoId(101L).build());
    repository.save(LikeEntity.builder().userId(1L).videoId(102L).build());
    repository.save(LikeEntity.builder().userId(2L).videoId(103L).build());

    List<LikeEntity> result=repository.findByUserId(1L);

    assertEquals(2, result.size());
    assertTrue(result.stream().anyMatch(l -> l.getVideoId().equals(101L)));
    assertTrue(result.stream().anyMatch(l -> l.getVideoId().equals(102L)));
}

}
