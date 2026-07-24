package com.teakter.comments.service;

import com.teakter.comments.dto.CommentDtoEntrada;
import com.teakter.comments.dto.CommentsDto;
import com.teakter.comments.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {

     CommentsDto saveComments(CommentDtoEntrada comment);

     List<CommentsDto> getCommentsByVideo(Long videoId, Long lastId, Integer limit);

     Long countCommentsInVideo(Long videoId);

     void  deleteComment(Long id);

     CommentsDto editComment(Long id, UpdateCommentDto content);

     void deleteCommentsWithVideoId(Long videoId);

     void deleteCommentsByUserId(Long userId);

}
