package com.teakter.video.dto;

import java.io.Serializable;

public record VideoProcessEvent(
        Long videoId,
        String rawFilename
) implements Serializable {
}
