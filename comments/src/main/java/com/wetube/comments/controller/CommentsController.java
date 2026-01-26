package com.wetube.comments.controller;

import com.wetube.comments.dto.CommentDtoEntrada;
import com.wetube.comments.dto.CommentsDto;
import com.wetube.comments.dto.UpdateCommentDto;
import com.wetube.comments.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
public class CommentsController {

private final CommentService commentService;

@Autowired
    public CommentsController(CommentService commentService){
    this.commentService=commentService;
}

//crear un comentario
    @PostMapping
    public ResponseEntity<CommentsDto> createComment(@Valid @RequestBody CommentDtoEntrada comment){
    return ResponseEntity.status(HttpStatus.CREATED).body(commentService.saveComments(comment));
    }

    //obtener todos los comentarios de un video
    @GetMapping("/{videoId}")
    public ResponseEntity<List<CommentsDto>> getCommentsVideo(@PathVariable Long videoId, @RequestParam(required = false) Long lastId, @RequestParam(defaultValue = "10") Integer limit){
    return ResponseEntity.ok(commentService.getCommentsByVideo(videoId, lastId, limit));
    }

    //eliminar un comentario
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id){
commentService.deleteComment(id);
    return ResponseEntity.ok("comentario eliminado correctamente");
    }

//editar un comentario
    @PutMapping("/{id}")
    public ResponseEntity<CommentsDto> updateComment(@PathVariable Long id, @Valid @RequestBody UpdateCommentDto content){
    return ResponseEntity.ok(commentService.editComment(id, content));
    }

}
