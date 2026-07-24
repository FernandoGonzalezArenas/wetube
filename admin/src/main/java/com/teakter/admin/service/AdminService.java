package com.teakter.admin.service;

import com.teakter.admin.dto.ReportDetailDto;
import com.teakter.admin.entity.PredefinedReason;
import com.teakter.admin.entity.ReportType;

import java.util.List;

public interface AdminService {

void moderateVideo(Long videoId, String reason);
void moderateUser(Long userId, String reason);
void createReport(ReportType type, Long targetId, PredefinedReason reason, String description);
List<ReportDetailDto> getPendingReports();
void dismissReport(Long targetId, ReportType type);

}
