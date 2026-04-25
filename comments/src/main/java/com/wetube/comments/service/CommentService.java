package com.wetube.comments.service;

import com.wetube.comments.dto.CommentDtoEntrada;
import com.wetube.comments.dto.CommentsDto;
import com.wetube.comments.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {

     CommentsDto saveComments(CommentDtoEntrada comment);

     List<CommentsDto> getCommentsByVideo(Long videoId, Long lastId, Integer limit);

     Long countCommentsInVideo(Long videoId);

     void  deleteComment(Long id);

     CommentsDto editComment(Long id, UpdateCommentDto content);

     void deleteCommentsWithVideoId(Long videoId);

}
