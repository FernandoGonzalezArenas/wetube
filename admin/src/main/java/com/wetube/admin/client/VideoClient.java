package com.wetube.admin.client;

import com.wetube.admin.dto.VideoMetadataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "video")
public interface VideoClient {

@DeleteMapping("/videos/internal/{id}")
    void deleteVideoInternal(@PathVariable("id") Long id);

@GetMapping("/videos/internal/details/{id}")
    VideoMetadataDto getVideoDetails(@PathVariable("id") Long id);

}
