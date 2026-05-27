package com.wetube.video.service;

import com.wetube.video.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VideoService {

    UploadUrlResponse generateUploadUrl(String filename);

    UploadUrlResponse generateUploadUrlThumb(String filename);

 VideoDto saveVideoMetadata(VideoDtoEntrada entrada);

Page<VideoDto> searchVideosByTitle(String keyword, String type, int page, int size);

List<VideoDto> getShortsFeed(Long lastId, int limit);

    List<VideoDto> getLongsFeed(Long lastId, int limit);

 InteractionsDto getInteractions(Long videoId, Long lastId, Integer limit);

 List<VideoDto> getVideosByIds(Long userId, Long lastId, int limit);

 VideoPlaybackDto getVideoForPlayback(Long videoId);

 List<VideoDto> getSubscriptionsFeed(Long userId, Long lastId, int limit);

 List<VideoDto> getVideosByUser(Long userId, Long lastId, int limit);

 void deleteVideoInternal(Long videoId);

VideoDto videoInternalDetails(Long videoId);

}
