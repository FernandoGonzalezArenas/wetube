package com.wetube.video.service;

import com.wetube.video.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VideoService {

    UploadUrlResponse generateUploadUrl(String filename);

 VideoDto saveVideoMetadata(VideoDtoEntrada entrada);

Page<VideoDto> searchVideosByTitle(String keyword, int page, int size);

List<VideoDto> getFeed(Long lastId, int limit);

 InteractionsDto getInteractions(Long videoId, Long lastId, Integer limit);

 List<VideoDto> getVideosByIds(IdsDto ids);

 VideoPlaybackDto getVideoForPlayback(Long videoId);

 List<VideoDto> getSubscriptionsFeed();

}
