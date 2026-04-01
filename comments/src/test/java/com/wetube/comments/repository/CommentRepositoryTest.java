package com.wetube.comments.repository;

import com.wetube.comments.entity.CommentEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CommentRepositoryTest {

@Autowired
    private CommentRepository repository;

@Autowired
private TestEntityManager entityManager;

@Test
    void findNextComments_ShouldReturnOnlyCommentsWithLowerId(){
saveComments(1L, "usuario1", "comentario 1");
saveComments(1L, "usuario1", "comentario 2");
saveComments(1L, "usuario1", "comentario 3");
    List<CommentEntity> result=repository.findNextComments(1L, 3L, PageRequest.of(0, 10));

//validaciones
    assertEquals(2, result.size());
    assertTrue(result.get(0).getId() < 3);
    assertEquals(2L, result.get(0).getId());
}

private void saveComments(Long videoId, String author, String content){
    CommentEntity c=new CommentEntity();
    c.setVideoId(videoId);
    c.setUsernameAuthor(author);
    c.setContent(content);
    repository.save(c);
}

@Test
    @DisplayName("debe eliminar fisicamente comentarios de un video")
    void shouldDeleteCommentsOfVideo(){
    CommentEntity comment=repository.save(CommentEntity.builder()
            .videoId(1L).usernameAuthor("yo").content("mi comentario").build());
    Long id=comment.getId();

    entityManager.flush();

    repository.deleteByVideoId(1L);

    entityManager.clear();
    Optional<CommentEntity> deleted=repository.findById(id);

    assertTrue(deleted.isEmpty());
}

}
