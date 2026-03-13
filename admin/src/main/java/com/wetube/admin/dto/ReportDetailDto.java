package com.wetube.admin.dto;

import com.wetube.admin.entity.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailDto {
private Long reportId; //id de el reporte
private ReportType type;
    private Long targetId;
    private String reason;

    private String videoTitle;
private String videoDescription;
private String videoUrl;
private String thumbnailUrl;

private String targetUsername;

private String status;
private LocalDateTime createdAt;

}
