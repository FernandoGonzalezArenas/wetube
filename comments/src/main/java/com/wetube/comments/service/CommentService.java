package com.wetube.comments.service;

import com.wetube.comments.dto.CommentDtoEntrada;
import com.wetube.comments.dto.CommentsDto;
import com.wetube.comments.dto.UpdateCommentDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CommentService {

     CommentsDto saveComments(CommentDtoEntrada comment, HttpServletRequest request);

     List<CommentsDto> getCommentsByVideo(Long videoId, Long lastId, int limit);

     void  deleteComment(Long id, HttpServletRequest request);

     CommentsDto editComment(Long id, UpdateCommentDto content, HttpServletRequest request);

}
