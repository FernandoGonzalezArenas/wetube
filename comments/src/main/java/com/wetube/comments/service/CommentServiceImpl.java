package com.wetube.comments.service;

import com.wetube.comments.dto.CommentDtoEntrada;
import com.wetube.comments.dto.CommentsDto;
import com.wetube.comments.dto.UpdateCommentDto;
import com.wetube.comments.entity.CommentEntity;
import com.wetube.comments.repository.CommentRepository;
import com.wetube.comments.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

@Autowired
    public CommentServiceImpl(CommentRepository commentRepository, JwtUtil jwtUtil){
    this.commentRepository=commentRepository;
    this.jwtUtil=jwtUtil;
}

//guardar un comentario
@Override
    public CommentsDto saveComments(CommentDtoEntrada comment, HttpServletRequest request){
String username= jwtUtil.getUsernameOrThrow(request);
    if (comment.getVideoId() == null) {
        throw new IllegalArgumentException("el videoId es obligatorio para poder guardar un comentario correctamente");
    }
    CommentEntity comentario=new CommentEntity();
    comentario.setUsernameAuthor(username);
    comentario.setVideoId(comment.getVideoId());
    comentario.setContent(comment.getContent());

    CommentEntity savedComment = commentRepository.save(comentario);
    return mapToDto(savedComment);
}

//obtener los comentarios de un video
    @Override
    public List<CommentsDto> getCommentsByVideo(Long videoId, Long lastId, int limit){
        Pageable pageable= PageRequest.of(0, limit);
        List<CommentEntity> comments= commentRepository.findNextComments(videoId, lastId, pageable);
        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    //eliminar un comentario
@Override
    public void  deleteComment(Long id, HttpServletRequest request){
    String username=jwtUtil.getUsernameOrThrow(request);
CommentEntity comment= commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "comentario no encontrado"));

//validacion de autoría
    if (!comment.getUsernameAuthor().equals(username)){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "no puedes borrar un comentario ajeno");
    }

    commentRepository.deleteById(id);
    }

    //editar un comentario
@Override
    public CommentsDto editComment(Long id, UpdateCommentDto content, HttpServletRequest request){
String username= jwtUtil.getUsernameOrThrow(request);
CommentEntity comment=commentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    if (!comment.getUsernameAuthor().equals(username)){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "no tienes permiso para modificar este comentario");
    }
    comment.setContent(content.getContent());
    comment.setUpdatedAt(LocalDateTime.now());
    return mapToDto(commentRepository.save(comment));
 }

 private CommentsDto mapToDto(CommentEntity comment){
return CommentsDto.builder()
        .videoId(comment.getVideoId())
        .usernameAuthor(comment.getUsernameAuthor())
        .content(comment.getContent())
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt()).build();
 }

}
