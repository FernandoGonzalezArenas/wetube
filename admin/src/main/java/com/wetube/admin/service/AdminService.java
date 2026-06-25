package com.wetube.admin.service;

import com.wetube.admin.dto.ReportDetailDto;
import com.wetube.admin.entity.PredefinedReason;
import com.wetube.admin.entity.ReportType;

import java.util.List;

public interface AdminService {

void moderateVideo(Long videoId, String reason);
void moderateUser(Long userId, String reason);
void createReport(ReportType type, Long targetId, PredefinedReason reason, String description);
List<ReportDetailDto> getPendingReports();
void dismissReport(Long targetId, ReportType type);

}
