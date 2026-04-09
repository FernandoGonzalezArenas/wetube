package com.wetube.comments.service;

import com.wetube.comments.dto.CommentDtoEntrada;
import com.wetube.comments.dto.CommentsDto;
import com.wetube.comments.dto.UpdateCommentDto;
import com.wetube.comments.dto.UserPrincipal;
import com.wetube.comments.entity.CommentEntity;
import com.wetube.comments.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

//guardar un comentario
@Override
    public CommentsDto saveComments(CommentDtoEntrada comment){
    UserPrincipal principal=(UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username=principal.username();
    CommentEntity comentario=new CommentEntity();
    comentario.setUsernameAuthor(username);
    comentario.setVideoId(comment.getVideoId());
    comentario.setContent(comment.getContent());

    CommentEntity savedComment = commentRepository.save(comentario);
    return mapToDto(savedComment);
}

//obtener los comentarios de un video
    @Override
    public List<CommentsDto> getCommentsByVideo(Long videoId, Long lastId, Integer limit){
        Pageable pageable= PageRequest.of(0, limit);
        List<CommentEntity> comments= commentRepository.findNextComments(videoId, lastId, pageable);
        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    //eliminar un comentario
@Override
    public void  deleteComment(Long id){
UserPrincipal principal=(UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String username=principal.username();

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
    public CommentsDto editComment(Long id, UpdateCommentDto content){
UserPrincipal principal =(UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String username=principal.username();

    CommentEntity comment=commentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    if (!comment.getUsernameAuthor().equals(username)){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "no tienes permiso para modificar este comentario");
    }
    comment.setContent(content.getContent());
    return mapToDto(commentRepository.save(comment));
 }

 @Override
 @Transactional
 public void deleteCommentsWithVideoId(Long videoId){
commentRepository.deleteByVideoId(videoId);
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
