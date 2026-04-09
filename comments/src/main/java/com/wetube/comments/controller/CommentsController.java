package com.wetube.comments.controller;

import com.wetube.comments.dto.CommentDtoEntrada;
import com.wetube.comments.dto.CommentsDto;
import com.wetube.comments.dto.UpdateCommentDto;
import com.wetube.comments.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
@RequiredArgsConstructor
@Tag(name = "Comments Controller", description = "gestiona la creacion, actualizacion, lectura y eliminacion de comentarios")
public class CommentsController {

private final CommentService commentService;

//crear un comentario
@Operation(summary = "crear un comentario",
        description = "se crea un comentario en un video")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "comentario creado exitosamente",
                content = @Content(schema = @Schema(implementation = CommentsDto.class))),
                @ApiResponse(responseCode = "400", description = "datos de el comentario incompletos")
                })
    @PostMapping
    public ResponseEntity<CommentsDto> createComment(@Valid @RequestBody CommentDtoEntrada comment){
    return ResponseEntity.status(HttpStatus.CREATED).body(commentService.saveComments(comment));
    }

    //obtener todos los comentarios de un video
    @Operation(summary = "obtener todos los comentarios de un video",
            description = "se obtienen todos los comentarios de un video para mostrarlos en el mismo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comentarios de el video obtenidos exitosamente",
                    content = @Content(schema = @Schema(implementation = CommentsDto.class)))
                    })
    @GetMapping("/{videoId}")
    public ResponseEntity<List<CommentsDto>> getCommentsVideo(@PathVariable Long videoId, @RequestParam(required = false) Long lastId, @RequestParam(defaultValue = "10") Integer limit){
    return ResponseEntity.ok(commentService.getCommentsByVideo(videoId, lastId, limit));
    }

    //eliminar un comentario
    @Operation(summary = "eliminar un comentario",
            description = "se elimina un comentario deseado por el autor de el mismo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comentario eliminado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "el comentario que se quiere eliminar no existe"),
            @ApiResponse(responseCode = "403", description = "no se tiene autorizacion para borrar este comentario")
                    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id){
commentService.deleteComment(id);
    return ResponseEntity.ok("comentario eliminado correctamente");
    }

//editar un comentario
@Operation(summary = "editar un comentario",
        description = "se edita un comentario siendo el autor")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "comentario editado exitosamente",
                content = @Content(schema = @Schema(implementation = CommentsDto.class))),
        @ApiResponse(responseCode = "400", description = "datos de el comentario incompletos"),
                @ApiResponse(responseCode = "404", description = "el comentario solicitado no existe"),
                @ApiResponse(responseCode = "403", description = "no se tiene autorizacion para editar este comentario")
                })
    @PutMapping("/{id}")
    public ResponseEntity<CommentsDto> updateComment(@PathVariable Long id, @Valid @RequestBody UpdateCommentDto content){
    return ResponseEntity.ok(commentService.editComment(id, content));
    }

}
