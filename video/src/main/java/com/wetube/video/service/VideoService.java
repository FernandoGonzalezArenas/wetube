package com.wetube.video.service;

import com.wetube.video.dto.InteractionsDto;
import com.wetube.video.dto.UploadUrlResponse;
import com.wetube.video.dto.VideoDto;
import com.wetube.video.dto.VideoDtoEntrada;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VideoService {

    UploadUrlResponse generateUploadUrl(String filename);

 VideoDto saveVideoMetadata(VideoDtoEntrada entrada, HttpServletRequest request);

Page<VideoDto> searchVideosByTitle(String keyword, int page, int size);

List<VideoDto> getFeed(Long lastId, int limit);

 InteractionsDto getInteractions(Long videoId);

}
